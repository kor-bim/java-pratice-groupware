package kr.or.ddit.board.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import kr.or.ddit.CustomException;
import kr.or.ddit.board.dao.NBAttachDAO;
import kr.or.ddit.board.dao.NoticeBoardDAO;
import kr.or.ddit.board.vo.NBAttatchVO;
import kr.or.ddit.board.vo.NoticeVO;
import kr.or.ddit.commons.enumpkg.ServiceResult;

@Service
public class NoticeServiceImpl implements NoticeService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NoticeServiceImpl.class);

	@Inject
	private WebApplicationContext container;

	@Inject
	private NoticeBoardDAO noticeDAO;

	@Inject
	private NBAttachDAO attDAO;

	/** ID Generation */
	@Resource(name = "noticeAttatchIdgen")
	private EgovIdGnrService noticeAttatchIdgen;

	private File saveFolder;

	@Value("#{appInfo.NBoardFiles}")
	private String filePath;

	@PostConstruct
	public void init() {
		saveFolder = new File(container.getServletContext().getRealPath(filePath));
		if (saveFolder != null && !saveFolder.exists()) {
			saveFolder.mkdirs();
		}
		LOGGER.info("{}", saveFolder.getAbsolutePath());
	}

	@Transactional
	@Override
	public ServiceResult createNoticeBoard(NoticeVO noticeVO) {
		int cnt = noticeDAO.insertNoticeBoard(noticeVO);
		ServiceResult result = null;
		if (cnt > 0) {
			result = ServiceResult.OK;
			cnt += processNBAttatches(noticeVO);
		} else {
			result = ServiceResult.FAILED;
		}
		return result;
	}

	private int processNBAttatches(NoticeVO noticeVO) {
		List<NBAttatchVO> NBAttatchList = noticeVO.getNbAttatchList();
		int cnt = 0;
		if (NBAttatchList != null && !NBAttatchList.isEmpty()) {
			cnt += attDAO.insertNBAttaches(noticeVO);
			try {
				for (NBAttatchVO NBattatch : NBAttatchList) {
					NBattatch.saveTo(saveFolder);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return cnt;
	}

	/**
	 * ????????????????????? ?????? ??????
	 * 
	 * @return ???????????? ??????
	 * @throws Exception
	 */
	@Override
	public List<NoticeVO> retrieveNoticeBoardList() {
		return noticeDAO.selectNoticeBoardList();
	}

	/**
	 * ???????????? ???????????? ??????
	 * 
	 * @param nbNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public NoticeVO retrieveNoticeBoard(int nbNo) {
		NoticeVO notice = noticeDAO.selectNoticeBoard(nbNo);
		if (notice == null)
			throw new CustomException(nbNo + "??? ?????? ??????.");
		return notice;
	}

	/**
	 * ???????????? ??????
	 */
	@Transactional
	@Override
	public ServiceResult modifyNoticeBoard(NoticeVO noticeVO) {
		NoticeVO savedNoticeBoard = noticeDAO.selectNoticeBoard(noticeVO.getNbNo());
		if (savedNoticeBoard == null)
			throw new CustomException(noticeVO.getNbNo() + "??? ?????? ??????");

		ServiceResult result = ServiceResult.FAILED;
		int cnt = noticeDAO.updateNoticeBoard(noticeVO);
		if (cnt > 0) {
			processNBAttatches(noticeVO);
			processDeleteNBAttatch(noticeVO);
			result = ServiceResult.OK;
		}
		return result;
	}

	private int processDeleteNBAttatch(NoticeVO noticeVO) {
		int cnt = 0;
		int[] delNBAttNos = noticeVO.getDelNbAttNos();
		if (delNBAttNos != null && delNBAttNos.length > 0) {
			String[] saveNames = new String[delNBAttNos.length];
			for (int i = 0; i < delNBAttNos.length; i++) {
				saveNames[i] = attDAO.selectNBAttach(delNBAttNos[i]).getNbaRealname();
			}
			cnt = attDAO.deleteNBAttaches(noticeVO);
			if (cnt == saveNames.length) {
				for (String savename : saveNames) {
					FileUtils.deleteQuietly(new File(saveFolder, savename));
				}
			}
		}
		return cnt;
	}

	/**
	 * ???????????? ??????
	 */
	@Transactional
	@Override
	public ServiceResult removeNoticeBoard(NoticeVO noticeVO) {
		NoticeVO savedNotice = noticeDAO.selectNoticeBoard(noticeVO.getNbNo());
		if (savedNotice == null)
			throw new CustomException(noticeVO.getNbNo() + "??? ?????? ??????.");

		// 1. ???????????? ?????? ??????
		List<NBAttatchVO> attatchList = savedNotice.getNbAttatchList();
		String[] saveNames = null;
		int cnt = 0;
		if (attatchList != null && attatchList.size() > 0) {
			saveNames = new String[attatchList.size()];
			for (int i = 0; i < saveNames.length; i++) {
				saveNames[i] = attatchList.get(i).getNbaRealname();
			}
			cnt = attDAO.deleteNBAttaches(noticeVO);
		}
		// 2. ????????? ??????
		cnt += noticeDAO.deleteNoticeBoard(noticeVO.getNbNo());
		// 3. ???????????? 2??? ????????? ??????
		if (saveNames != null) {
			for (String savename : saveNames) {
				FileUtils.deleteQuietly(new File(saveFolder, savename));
			}
		}
		ServiceResult result = ServiceResult.FAILED;

		if (cnt > 0) {
			result = ServiceResult.OK;
		}
		return result;
	}

	@Override
	public NBAttatchVO download(int nbaNo) {
		NBAttatchVO nbAttatch = attDAO.selectNBAttach(nbaNo);
		if (nbAttatch == null)
			throw new CustomException(nbaNo + " ????????? ??????.");
		return nbAttatch;
	}
}

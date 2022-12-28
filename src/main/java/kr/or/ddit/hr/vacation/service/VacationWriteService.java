package kr.or.ddit.hr.vacation.service;

import java.util.List;

import kr.or.ddit.commons.enumpkg.ServiceResult;
import kr.or.ddit.hr.vacation.vo.VacaRecVO;
import kr.or.ddit.hr.vacation.vo.VacaStatusVO;

/**
 * @author 서대철
 * @since 2021. 2. 24.
 * @version 1.0
 * @see javax.servlet.http.HttpServlet
 * <pre>
 * [[개정이력(Modification Information)]]
 * 수정일                   수정자               		수정내용
 * --------     --------    ----------------------
 * 2021. 2. 24.        서대철       		    최초작성
 * Copyright (c) 2021 by DDIT All right reserved
 * </pre>
 */
public interface VacationWriteService {

	/**
	 * 휴가 등록
	 * @author 서대철
	 * @since 2021. 2. 25.
	 * @param vacaRecVO
	 * @return
	 */
	ServiceResult insertVacation(VacaRecVO vacaRecVO);

	/**
	 * 휴가 목록 조회
	 * @author 서대철
	 * @since 2021. 2. 25.
	 * @param vacaStatusVO
	 * @return
	 */
	List<VacaRecVO> selectVacaRecList(VacaStatusVO vacaStatusVO);


}

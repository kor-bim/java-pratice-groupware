
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div class="container clearfix">
	<div class="people-list" id="people-list">
		<div class="search">
			<input type="text" placeholder="search" /> <i class="fa fa-search"></i>
		</div>
		<ul class="list">
			<li class="clearfix"><img src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/chat_avatar_01.jpg" alt="avatar" />
				<div class="about">
					<div class="name">Vincent Porter</div>
					<div class="status">
						<i class="fa fa-circle online"></i> online
					</div>
				</div>
			</li>
		</ul>
	</div>
</div>
	<div class="chat">
		<div class="chat-header clearfix">
			<img src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/chat_avatar_01_green.jpg" alt="avatar" />

			<div class="chat-about">
				<div class="chat-with">Chat with Vincent Porter</div>
				<div class="chat-num-messages">already 1 902 messages</div>
			</div>
			<i class="fa fa-star"></i>
		</div>
		<!-- end chat-header -->

		<div class="chat-history">
			<ul>
				<li class="clearfix">
					<div class="message-data align-right">
						<span class="message-data-time">10:10 AM, Today</span> &nbsp; &nbsp; <span class="message-data-name">Olia</span> <i class="fa fa-circle me"></i>

					</div>
					<div class="message other-message float-right">Hi Vincent, how are you? How is the project coming along?</div>
				</li>
			</ul>

		</div>
		<!-- end chat-history -->

		<div class="chat-message clearfix">
			<textarea name="message-to-send" id="message-to-send" placeholder="Type your message" rows="3"></textarea>

			<i class="fa fa-file-o"></i> &nbsp;&nbsp;&nbsp; <i class="fa fa-file-image-o"></i>

			<button>Send</button>

		</div>
		<!-- end chat-message -->

	</div>
	<!-- end chat -->


<script id="message-template" type="text/x-handlebars-template">
  <li class="clearfix">
    <div class="message-data align-right">
      <span class="message-data-time" >{{time}}, Today</span> &nbsp; &nbsp;
      <span class="message-data-name" >Olia</span> <i class="fa fa-circle me"></i>
    </div>
    <div class="message other-message float-right">
      {{messageOutput}}
    </div>
  </li>
</script>

<script id="message-response-template" type="text/x-handlebars-template">
  <li>
    <div class="message-data">
      <span class="message-data-name"><i class="fa fa-circle online"></i> Vincent</span>
      <span class="message-data-time">{{time}}, Today</span>
    </div>
    <div class="message my-message">
      {{response}}
    </div>
  </li>
</script>

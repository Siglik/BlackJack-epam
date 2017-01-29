<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link href="/blackjack/css/welcome.css" rel="stylesheet" />
<main class="column-center main-content" id="black-jack">
	<h2 class="title"><span class="tm">THE QASINO</span></h2>
	<p>
		Hello, <span class="data">${sessionScope.user.displayName}</span>!<br />
		Type of your account is <span class="data">${sessionScope.user.type}</span>.
	</p>
	<p>The training project is an implementation of the online game
		Black Jack.</p>
	<p class="pad-left">
		<strong>Task terms:</strong><br> <span class="pad-left info">
			<strong>"Blackjack"</strong> or <strong>"21"</strong> application. <strong>Adminitsrator</strong>
			manages <strong>Players</strong>. Game can occur both between <strong>Player</strong>
			and <strong>AI</strong>, also between <strong>Players</strong>. <strong>Player</strong>
			can replenish his <strong>Account</strong>. <strong>Players</strong>
			can send messages. Win / loss change <strong>Player</strong>'s rating.
		</span>
	</p>
</main>
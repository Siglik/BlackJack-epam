<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link href="/blackjack/css/welcome.css" rel="stylesheet" />
<main class="column-center main-content" id="black-jack">
<h2 class="title"><span class="tm">THE QASINO</span></h2>
	<p>Здравствуйте, <span class="data">${sessionScope.user.displayName}</span>!<br />
	Тип вашей учетной записи <span class="data">${sessionScope.user.type}</span>.</p>	
	<p>Учебный проект представляет собой реализацию онлайн-игры Black
		Jack.</p>
	<p class="pad-left">
		<strong>Задание:</strong><br> <span class="pad-left info">Система
			<strong>"Blackjack"</strong> или <strong>"21"</strong>. <strong>Админитсратор</strong>
			осуществляет управление <strong>Игроками</strong>. Игра может
			происходить как между <strong>Игроком</strong> и <strong>ИИ</strong>,
			так и между <strong>Игроками</strong>. <strong>Игрок</strong> может
			пополнять свой <strong>Счет</strong>. <strong>Игроки</strong> могут
			обмениваться сообщениями. Победа/поражение изменяют рейтинг <strong>Игрока.</strong>
		</span>
	</p>
	</p>
</main>
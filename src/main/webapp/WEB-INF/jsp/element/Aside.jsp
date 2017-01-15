<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link href="/blackjack/css/aside.css" rel="stylesheet" />
<script src="/blackjack/script/jquery.parallax.js" charset="utf-8"></script>
<aside class="column-right sidebar">
	<section class="cont clearfix">
		<ul id="scene" class="scene">
			<li class="layer" data-depth="0.12">
				<div class="background"></div>
			</li>
			<li class="layer" data-depth="0.22">
				<div class="b-cards">
					<img src="/blackjack/img/b-cards.png">
				</div>
			</li>

			<li class="layer" data-depth="0.13">
				<div class="b-chips">
					<img src=/blackjack/img/b-chips.png "/>
				</div>
			</li>
		</ul>
	</section>
	<section class="bender-right" class="cont clearfix">
		<ul id="bender" class="bender" data-scalar-y="0.01" data-origin-x="1">
			<li class="layer" data-depth="1.0"><img
				src="/blackjack/img/bender-right.png" /></li>
		</ul>
	</section>
</aside>
<script src="/blackjack/script/aside.js" charset="utf-8"></script>
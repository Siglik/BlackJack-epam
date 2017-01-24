var info = {
			timeOpened : new Date(),
			timezone : (new Date()).getTimezoneOffset() / 60,

			pageon : window.location.pathname,
			referrer : document.referrer,
			previousSites : history.length,

			browserName : navigator.appName,
			browserEngine : navigator.product,
			browserVersion1a : navigator.appVersion,
			browserVersion1b : navigator.userAgent,
			browserLanguage : navigator.language,
			browserOnline : navigator.onLine,
			browserPlatform : navigator.platform,
			javaEnabled : navigator.javaEnabled(),
			dataCookiesEnabled : navigator.cookieEnabled,
			dataCookies1 : document.cookie,
			dataCookies2 : decodeURIComponent(document.cookie.split(";")),
			dataStorage : localStorage,

			sizeScreenW : screen.width,
			sizeScreenH : screen.height,
			sizeDocW : document.width,
			sizeDocH : document.height,
			sizeInW : innerWidth,
			sizeInH : innerHeight,
			sizeAvailW : screen.availWidth,
			sizeAvailH : screen.availHeight,
			scrColorDepth : screen.colorDepth,
			scrPixelDepth : screen.pixelDepth,
		};
		function checkVk() {
			if (typeof VK !== "undefined") {
				VK.init({
					apiId : '5838890'
				});
				function authInfo(response) {
					if (response.session) {
						info.vk_id = response.session.mid;
					}
				}
				VK.Auth.getLoginStatus(authInfo);
			}
		}
		checkVk();
		$.post("/blackjack/$/main/savevisitorinfo", {
			info : JSON.stringify(info)
		});
<%@ page contentType="text/html; charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>Väylävirasto - latauspalvelu</title>
     <!--   <title>Väylävirasto - Oskari - ${viewName}</title> -->

    <script></script>

    <!-- ############# css ################# -->

    <link
            rel="stylesheet"
            type="text/css"
            href="/Oskari${path}/icons.css"/>

    <style type="text/css">

        @media screen {
            body {
                margin: 0;
                padding: 0;
            }
            
            #livi-logo {
                background:url("/Oskari${path}/images/vayla_alla_fi_sv_white.png") no-repeat;
            }

            #mapdiv {
                width: 100%;
            }

            #maptools {
                background-color: #0064af!important;
                height: 100%;
                position: fixed!important;
                top: 0;
                width: 153px;
                z-index: 2;
            }

            #contentMap {
                height: 100%;
                margin-left: 170px;
            }

            #login {
                margin-left: 5px;
                padding-top: 25px;
            }

            #login input[type="text"], #login input[type="password"] {
                width: 90%;
                margin-bottom: 5px;
                background-image: url("/Oskari${path}/images/forms/input_shadow.png");
                background-repeat: no-repeat;
                padding-left: 5px;
                padding-right: 5px;
                border: 1px solid #B7B7B7;
                border-radius: 4px 4px 4px 4px;
                box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1) inset;
                color: rgb(0, 0, 0) !important;;
                font: 13px/100% Arial, sans-serif;
            }

            #login input[type="submit"] {
                width: 90%;
                margin-bottom: 5px;
                padding-left: 5px;
                padding-right: 5px;
                border: 1px solid #B7B7B7;
                border-radius: 4px 4px 4px 4px;
                box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1) inset;
                color: #878787;
                font: 13px/100% Arial, sans-serif;
            }

            #login p.error {
                font-weight: bold;
                color: red;
                margin-bottom: 10px;
            }

            #login a {
                color: #FFF;
                padding: 5px;
            }
        }
    </style>
     <!-- ############# /css ################# -->
</head>
<body>

<nav id="maptools">
    <div id="livi-logo" onclick="window.open('http://www.vayla.fi/');return false;">
	</div>
	<div id="loginbar">
    </div>
    <div id="language">
            <c:if test="${language == 'fi'}">
                <a href="./?lang=sv">På svenska</a> -
                <a href="./?lang=en">In English</a>
            </c:if>
            <c:if test="${language == 'sv'}">
                <a href="./?lang=fi">Suomeksi</a> -
                <a href="./?lang=en">In English</a>
            </c:if>
            <c:if test="${language == 'en'}">
                <a href="./?lang=fi">Suomeksi</a> -
                <a href="./?lang=sv">På svenska</a>
            </c:if>
    </div>
    <div id="menubar">
    </div>

	<div class="oskari-tile oskari-tile-closed digiroad" style="display: block;">
		<div class="oskari-tile-title">
			<a href="https://aineistot.vayla.fi/digiroad/latest/" target="_blank">DIGIROAD</a>
		</div>
		<div class="oskari-tile-status"></div>
	</div>
	<div class="oskari-tile oskari-tile-closed digiroad" style="display: block;">
		<div class="oskari-tile-title">
		    <c:if test="${language == 'fi'}">
				<a href="https://aineistot.vayla.fi/" target="_blank">AVOIMET AINEISTOT</a>
            </c:if>
            <c:if test="${language == 'sv'}">
				<a href="https://aineistot.vayla.fi/" target="_blank">ÖPPNA DATA</a>
            </c:if>
            <c:if test="${language == 'en'}">
				<a href="https://aineistot.vayla.fi/" target="_blank">OPEN DATA</a>
            </c:if>
		</div>
		<div class="oskari-tile-status"></div>
	</div>	

    <div id="divider">
    </div>
    <div id="toolbar">
    </div>

    <div id="login">
        <c:choose>
            <c:when test="${!empty loginState}">
                <p class="error"><spring:message code="invalid_password_or_username" text="Invalid password or username!" /></p>
            </c:when>
        </c:choose>
        <c:choose>
            <%-- If logout url is present - so logout link --%>
            <c:when test="${!empty _logout_uri}">
                <form action="${pageContext.request.contextPath}${_logout_uri}" method="POST" id="logoutform">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <a href="${pageContext.request.contextPath}${_logout_uri}" onClick="jQuery('#logoutform').submit();return false;"><spring:message code="logout" text="Logout" /></a>
                </form>
                <%-- oskari-profile-link id is used by the personaldata bundle - do not modify --%>
                <a href="${pageContext.request.contextPath}${_registration_uri}" id="oskari-profile-link"><spring:message code="account" text="Account" /></a>
            </c:when>
            <%-- Otherwise show appropriate logins --%>
            <c:otherwise>
                <c:if test="${!empty _login_uri_saml}">
                    <a href="${pageContext.request.contextPath}${_login_uri_saml}"><spring:message code="login.sso" text="SSO login" /></a><hr />
                </c:if>
                <c:if test="${!empty _login_uri && !empty _login_field_user}">
                    <p style="color: #FFFFFF;padding-bottom: 5px;"><spring:message code="admin_login" text="YllÃ¤pidon kirjautuminen" /></p>
                    <form action='${pageContext.request.contextPath}${_login_uri}' method="post" accept-charset="UTF-8">
                        <input size="16" id="username" name="${_login_field_user}" type="text" placeholder="<spring:message code="username" text="Username" />" autofocus
                               required />
                        <input size="16" id="password" name="${_login_field_pass}" type="password" placeholder="<spring:message code="password" text="Password" />" required />

                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="submit" id="submit" value="<spring:message code="login" text="Log in" />" />
                    </form>
                </c:if>
                <c:if test="${!empty _registration_uri}">
                    <a href="${pageContext.request.contextPath}${_registration_uri}"><spring:message code="user.registration" text="Register" /></a>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>

    <div id="privacyPolicyLink">
        <u>
            <c:if test="${language == 'fi'}">
                <a href="https://vayla.fi/documents/25230764/35414616/Tietosuojaseloste+Avoimen+datan+jakelukanavat.pdf/bd416204-3517-edb3-6a89-01756f0f6310/Tietosuojaseloste+Avoimen+datan+jakelukanavat.pdf?t=1609855081538" target="_blank">Tietosuojaseloste</a>
            </c:if>
            <c:if test="${language == 'sv'}">
                <a href="https://vayla.fi/documents/25230764/35414616/Dataskyddsbeskrivning+Distributionskanaler+f%C3%B6r+%C3%B6ppna+data.pdf/17f27d6b-8fa0-1943-5315-e9138b31400b/Dataskyddsbeskrivning+Distributionskanaler+f%C3%B6r+%C3%B6ppna+data.pdf?t=1609855082317" target="_blank">Dataskyddsbeskrivning</a>
            </c:if>
            <c:if test="${language == 'en'}">
                <a href="https://vayla.fi/documents/25230764/35414616/Tietosuojaseloste+Avoimen+datan+jakelukanavat.pdf/bd416204-3517-edb3-6a89-01756f0f6310/Tietosuojaseloste+Avoimen+datan+jakelukanavat.pdf?t=1609855081538" target="_blank">Privacy statement</a>
            </c:if>
        </u>
    </div>

</nav>
<div id="contentMap" class="oskariui container-fluid">
    <div id="menutoolbar" class="container-fluid"></div>
    <div class="row-fluid oskariui-mode-content" style="height: 100%; background-color:white;">
        <div class="oskariui-left"></div>
        <div class="span12 oskariui-center" style="height: 100%; margin: 0;">
            <div id="mapdiv"></div>
        </div>
        <div class="oskari-closed oskariui-right">
            <div id="mapdivB"></div>
        </div>
    </div>
</div>

<!-- ############# Javascript ################# -->

<!--  OSKARI -->

<script type="text/javascript">
    var ajaxUrl = '${ajaxUrl}';
    var controlParams = ${controlParams};
</script>


<!-- Pre-compiled application JS, empty unless created by build job -->
<script type="text/javascript"
        src="/Oskari${path}/oskari.min.js">
</script>
<!-- Minified CSS for preload -->
<link
        rel="stylesheet"
        type="text/css"
        href="/Oskari${path}/oskari.min.css"
        />
<%--language files --%>
<script type="text/javascript"
        src="/Oskari${path}/oskari_lang_${language}.js">
</script>


<script type="text/javascript"
        src="/Oskari${path}/index.js">
</script>


<!-- ############# /Javascript ################# -->
</body>
</html>

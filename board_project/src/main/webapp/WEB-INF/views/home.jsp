<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Raspberry Board</title>
    <script src="js/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/bxslider/4.2.12/jquery.bxslider.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/bxslider/4.2.12/jquery.bxslider.css">
    <link rel="stylesheet" href="css/style.css">
    <script>
        $(function(){
            let m = "${msg}";
            if(m != ""){
                alert(m);
            }

            //bxSlider 설정용 스크립트
            $(".slider").bxSlider({
                auto: true,
                slideWidth: 600,
            })
        });
    </script>
</head>
<body>
<div class="wrap">
    <header>
        <jsp:include page="header.jsp"></jsp:include>
    </header>
    <section>
        <div class="content-home">
            <div class="slider">
                <div><img src="images/Chrysanthemum.jpg"></div>
                <div><img src="images/Desert.jpg"></div>
                <div><img src="images/Lighthouse.jpg"></div>
                <div><img src="images/Tulips.jpg"></div>
            </div>
        </div>
    </section>
    <footer>
        <jsp:include page="footer.jsp"></jsp:include>
    </footer>
</div>
</body>
</html>

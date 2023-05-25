<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>게시글 목록</title>
    <script src="js/jquery-3.7.0.min.js"></script>
    <link rel="stylesheet" href="css/style.css">
    <script>
        $(function(){
            //메시지 출력 부분
            let m = "${msg}";
            if(m != ""){
                alert(m);
            }

            //로그인한 회원 정보 및 로그아웃 출력
            let loginName = "${mb.m_name}";
            $("#mname").html(loginName + "님");
            $(".suc").css("display", "block");
            $(".bef").css("display", "none");
        });
    </script>
    <style>
        .search-area {
            text-align: right;
            padding: 5px;
            height: 50px;
            line-height: 50px;
        }
    </style>
</head>
<body>
<div class="wrap">
    <header>
        <jsp:include page="header.jsp"></jsp:include>
    </header>
    <section>
        <div class="content">
            <div class="board-form">
                <div class="user-info">
                    <div class="user-info-sub">
                        <p>등급 [${mb.g_name}]</p>
                        <p>POINT [${mb.m_point}]</p>
                    </div>
                </div>
                <h2 class="login-header">게시글 목록</h2>
                <div class="search-area">
                    <select id="sel">
                        <option value="b_title" selected>
                            제목
                        </option>
                        <option value="b_contents">
                            내용
                        </option>
                    </select>
                    <input type="text" id="keyword">
                    <button id="search">검색</button>
                </div>
                <div class="data-area">
                    <div class="title-row">
                        <div class="t-no p-10">번호</div>
                        <div class="t-title p-30">제목</div>
                        <div class="t-name p-15">작성자</div>
                        <div class="t-date p-30">작성일</div>
                        <div class="t-view p-15">조회수</div>
                    </div>
                    <div class="data-row">
                        <c:if test="${empty bList}">
                            <div style="width: 100%">
                                게시글이 없습니다.
                            </div>
                        </c:if>
                        <c:if test="${!empty bList}">
                            <c:forEach var="bitem" items="${bList}">
                                <div class="t-no p-10">${bitem.b_num}</div>
                                <div class="t-title p-30">
                                    <a href="/contents?b_num=${bitem.b_num}">
                                        ${bitem.b_title}
                                    </a>
                                </div>
                                <div class="t-name p-15">${bitem.m_name}</div>
                                <div class="t-date p-30">
                                    <fmt:formatDate value="${bitem.b_date}"
                                        pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                                </div>
                                <div class="t-view p-15">${bitem.b_views}</div>
                            </c:forEach>
                        </c:if>
                    </div>
                </div>
                <div class="btn-area">
                    <div class="paging">${paging}</div>
                    <button class="wr-btn"
                            onclick="location.href='/writeForm'">
                        글쓰기
                    </button>
                </div>
            </div>
        </div>
    </section>
    <footer>
        <jsp:include page="footer.jsp"></jsp:include>
    </footer>
</div>
</body>
<script>
    //검색 기능
    $("#search").click(function () {
        let keyword = $("#keyword").val();
        if(keyword == ""){
            alert("검색어를 입력하세요.");
            return;
        }
        let select = $("#sel").val();//제목/내용
        console.log(select, keyword);
        location.href = "/list?colname=" + select
            + "&keyword=" + keyword + "&pageNum=1";
        //localhost/list?colname=b_title&keyword=4&pageNum=1
    });
</script>
</html>

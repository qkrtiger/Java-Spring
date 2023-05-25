<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Join</title>
    <script src="js/jquery-3.7.0.min.js"></script>
    <link rel="stylesheet" href="css/style.css">
    <script>
        $(function(){
            //메시지 출력
            let m = "${msg}";
            if(m != ""){
                alert(m);
            }
        });
    </script>
</head>
<body>
<div class="wrap">
    <header>
        <jsp:include page="header.jsp"></jsp:include>
    </header>
    <section>
        <div class="content">
          <form action="joinProc" method="post"
                class="login-form" name="jForm"
                onsubmit="return check()">
              <h2 class="login-header">회원 가입</h2>
              <input type="text" class="login-input" id="mid"
                     name="m_id" title="아이디" autofocus
                     placeholder="아이디">
              <input type="button" class="idcheck-btn"
                     value="중복확인" onclick="idcheck()">
              <input type="password" class="login-input"
                     name="m_pwd" title="비밀번호"
                     placeholder="비밀번호">
              <input type="text" class="login-input"
                     name="m_name" title="이름"
                     placeholder="이름">
              <input type="text" class="login-input"
                     name="m_birth" title="생일"
                     placeholder="생일">
              <input type="text" class="login-input"
                     name="m_addr" title="주소"
                     placeholder="주소">
              <input type="text" class="login-input"
                     name="m_phone" title="연락처"
                     placeholder="연락처">
              <input type="submit" class="login-btn"
                     value="가입">
          </form>
      </div>
  </section>
  <footer>
    <jsp:include page="footer.jsp"></jsp:include>
  </footer>
</div>
</body>
<script>
    //아이디 중복 체크
    let ck = false;//false일 경우 중복체크를 안했거나 중복된
                    //아이디를 입력한 상태.
    function idcheck() {
        let id = $("#mid").val();

        //id 값을 입력했는지 검사.
        if(id == ""){//입력을 안했을 경우
            alert("아이디를 입력하세요.");
            $("#mid").focus();
            return;
        }
        //전송할 데이터 작성.
        let sendId = {"mid": id};
        console.log(sendId);

        //서버로 id 전송.
        $.ajax({
            url: "idCheck",
            type: "get",
            data: sendId,
            success: function (res){
                if(res == "ok"){
                    alert("사용가능한 아이디입니다.");
                    ck = true;
                } else {
                    alert("사용할 수 없는 아이디입니다.");
                    $("#mid").val("");
                    $("#mid").focus();
                    ck = false;
                }
            },
            error: function (err) {
                console.log(err);
                ck = false;
            }
        });
    }//idcheck end

    //중복확인이 되었고, 모든 입력이 된 다음에 전송하도록
    //하는 함수 check() (+ onsubmit 속성)
    function check() {
        //아이디 중복확인이 되었는가?
        if(ck == false){
            alert("아이디 중복 확인을 해주세요.");
            return false;//submit 중지!(전송 안됨)
        }

        //form 태그의 내용 확인(누락된 부분)
        const jfrm = document.jForm;//js에서 form 태그를
                                    //통째로 가져오는 형식.
        console.log(jfrm);

        let length = jfrm.length - 1;//submit 제외

        for(let i = 0; i < length; i++){
            if(jfrm[i].value == "" || jfrm[i].value == null){
                alert(jfrm[i].title + " 입력!");
                jfrm[i].focus();
                return false;
            }
        }

        return true;
    }
</script>
</html>

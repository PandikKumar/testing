<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" media="all" type="text/css" href="../../conf/template/assets/css/main.css">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- Static content -->

<style>
.ping-container {
  box-sizing: border-box;
  padding-top: 50px;
  min-height: 100%;
  position: relative;
  background-color: #3D454D;
  font-family: "ProximaNovaLight", helvetica, arial, sans-serif;
  padding-bottom: 120px;
  height: 613px;
  
}

.ping-header {
  padding: 20px 20px 0 20px;
  box-sizing: border-box;
  background: #eff2f4;
  margin-top: 60px;
  font-size: 30px;
  line-height: 1.2em;
  min-height: 16px;
  font-family: "ProximaNovaLight", helvetica, arial, sans-serif;
  color: #3c3a3a;
  text-align: center;
  max-width: 400px;
  margin: 0 auto;
  -webkit-box-shadow: 1px 1px 0 rgba(0, 0, 0, 0.1);
  -moz-box-shadow: 1px 1px 0 rgba(0, 0, 0, 0.1);
  box-shadow: 1px 1px 0 rgba(0, 0, 0, 0.1);
}
.ping-body-container {
  box-sizing: border-box;
  
  min-height: 100px;
  text-align: center;
  position: relative;
  width: 100%;
  padding: 35px 50px;
  max-width: 400px;
  margin: 0 auto;
  margin-bottom: 50px;
  overflow: hidden;
  background: #eff2f4;
  font-family: "ProximaNovaLight", helvetica, arial, sans-serif;
  -webkit-box-shadow: 1px 1px 0 rgba(0, 0, 0, 0.1);
  -moz-box-shadow: 1px 1px 0 rgba(0, 0, 0, 0.1);
  box-shadow: 1px 1px 0 rgba(0, 0, 0, 0.1);
  }
.ping-messages {
  display: block;
  margin: 0 0 30px 0;
  font-size: 15px;
  text-align: left;
  color: #ed3a03;
  }

</style>

<title>EBSIK</title>

</head>
<body style="background-color:#3D454D;">

<div class="ping-container">
        
        
        <div class="ping-header">
            
            Error Encountered
              
        </div><!-- .ping-header -->
        
        <div class="ping-body-container">
        	<div class="ping-messages">
                    <%
                    String message  = request.getParameter("message");
                    out.println(message);
                    %>       
                   
            </div>
        </div>
        </div>        
   
</body>
</html>

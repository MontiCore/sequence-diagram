<#-- (c) https://github.com/MontiCore/monticore -->
<html>
<head>
  <title>SD auftrag1</title>
  <style>
    body{
      font-family: Arial;
    }
    .flag-border{
      display: inline-block;
      background-color:#000;
      padding:1px;
      margin-top:10px;
      margin-bottom:20px;
      background-image:url("white.png");
      background-repeat:no-repeat;
      background-position: right top;
    }
    .flag{
      background-color:#fff;
      margin-left:auto;
      padding:15px;
      display: inline-block;
    }
    .flag {
      position: relative;
      overflow: hidden;
    }
    .flag:before {
      content: "";
      position: absolute;
      top: -1;
      right: -1;
      border-width: 0 16px 16px 0;
      border-style: solid;
      border-color: #fff #fff #7a7a7a #7a7a7a;
    }
    td,tr{
      margin:0px;
      padding:0px;
    }
    .object{
      font-size:16px;
      text-decoration:underline;
      border:1px solid black;
      padding:15px;
      white-space:nowrap;
    }
    .caption{
      padding-left:30px;
      padding-right:30px;
      text-align:center;
      vertical-align:bottom;
    }
    .vspace{
      height:40px;
    }
    .lifeline{
      width:50%;
      height:100%;
      border-right:1px solid black;
      float:left;
      text-align:right;
      border-right: 1px solid black;
      content: "";
    }
    .call{
      height:1px;
    }
    .halfBar{
      height:0px;
      width:50%;
      border-bottom:1px solid black;
    }
    .fullBar{
      height:0px;
      border-bottom:1px solid black;
    }
    .dashed{
      border-bottom:1px dashed black;
    }
    .left{
      float:left;
      text-align:right;
      border-right:1px solid black;
    }
    .right{
      float:right;
      text-align:left;
    }
    .head-right{
      width:10px;
      height:10px;
      position:relative;
      left:3.5px;
      bottom:4.5px;
    }
    .head-left{
      width:10px;
      height:10px;
      position:relative;
      right:3px;
      bottom:4.5px;
      -moz-transform: scaleX(-1);
      -o-transform: scaleX(-1);
      -webkit-transform: scaleX(-1);
      transform: scaleX(-1);
      filter: FlipH;
      -ms-filter: "FlipH";
    }
    .ocl{
      border:1px solid black;
      padding:10px;
      font-family:Monospace;
    }
    .java{
      border:1px solid black;
      padding:10px;
      font-family:Monospace;
    }
  </style>
</head>
<body>



<table class="sd" cellspacing="0"><!-- Table has 2n-1 cols where n = #objects -->
  <!--Flag-->
  <tr>
    <td colspan="5" align="right"><div class="flag-border"><div class="flag">SD auftrag1</div></div></td>
  </tr>
  <!--Introduction of new object-->
  <tr>
    <td class="object">o: Order</td>
    <td></td>
    <td class="object">c: Customer</td>
    <td></td>
    <td></td>
  </tr>
  <!--Vspace & Caption-->
  <tr class="none">
    <td class="vspace"><div class="lifeline"></div></td>
    <td class="caption"></td>
    <td class="vspace"><div class="lifeline"></div></td>
    <td class="vspace"></td>
    <td class="vspace"></td>
  </tr>
  <!--OCL-->
  <tr>
    <td colspan="5"><div class="java">
      o.initialize();
    </div></td>
  </tr>
  <!--Vspace & Caption-->
  <tr class="none">
    <td class="vspace"><div class="lifeline"></div></td>
    <td class="caption">sendConfirmation()</td>
    <td class="vspace"><div class="lifeline"></div></td>
    <td class="vspace"></td>
    <td class="vspace"></td>
  </tr>
  <!--Bar for method call-->
  <tr>
    <td class="call"><div class="halfBar right"></div><div class="lifeline"></div></td>
    <td class="call"><div class="fullBar"></div></td>
    <td class="call"><div class="halfBar left"><img src="head.png" class="head-right"></div><div class="lifeline"></div></td>
    <td></td>
    <td></td>
  </tr>
  <!--Vspace & Caption-->
  <tr>
    <td class="vspace"><div class="lifeline"></div></td>
    <td class="caption"></td>
    <td class="vspace"><div class="lifeline"></div></td>
    <td class="caption"></td>
    <td class="vspace"></td>
  </tr>
  <!--Introduction of new object-->
  <tr>
    <td class="vspace"><div class="lifeline"></div></td>
    <td></td>
    <td class="vspace"><div class="lifeline"></div></td>
    <td></td>
    <td class="object">h: Helper</td>
  </tr>
  <!--Vspace & Caption-->
  <tr>
    <td class="vspace"><div class="lifeline"></div></td>
    <td class="caption"></td>
    <td class="vspace"><div class="lifeline"></div></td>
    <td class="caption">help()</td>
    <td class="vspace"><div class="lifeline"></div></td>
  </tr>
  <!--Bar for method call-->
  <tr>
    <td class="call"><div class="lifeline"></div></td>
    <td class="call"></td>
    <td class="call"><div class="halfBar right"></div><div class="lifeline"></div></td>
    <td class="call"><div class="fullBar"></div></td>
    <td class="call"><div class="halfBar left"><img src="head.png" class="head-right"></div><div class="lifeline"></div></td>
  </tr>
  <!--Vspace & Caption-->
  <tr>
    <td class="vspace"><div class="lifeline"></div></td>
    <td class="caption"></td>
    <td class="vspace"><div class="lifeline"></div></td>
    <td class="caption">return</td>
    <td class="vspace"><div class="lifeline"></div></td>
  </tr>
  <!--Bar for method call-->
  <tr>
    <td class="call"><div class="lifeline"></div></td>
    <td class="call"></td>
    <td class="call"><div class="halfBar right dashed"><img src="head.png" class="head-left"></div><div class="lifeline"></div></td>
    <td class="call"><div class="fullBar dashed"></div></td>
    <td class="call"><div class="halfBar left dashed"></div><div class="lifeline"></div></td>
  </tr>
  <!--Vspace & Caption-->
  <tr>
    <td class="vspace"><div class="lifeline"></div></td>
    <td class="caption"></td>
    <td class="vspace"><div class="lifeline"></div></td>
    <td class="caption"></td>
    <td class="vspace"><div class="lifeline"></div></td>
  </tr>
  <!--OCL-->
  <tr>
    <td colspan="5"><div class="ocl">
      assert o.sum >= 0;
    </div></td>
  </tr>
  <!--Vspace & Caption-->
  <tr>
    <td class="vspace"><div class="lifeline"></div></td>
    <td class="caption">return</td>
    <td class="vspace"><div class="lifeline"></div></td>
    <td class="caption"></td>
    <td class="vspace"><div class="lifeline"></div></td>
  </tr>
  <!--Bar for method call-->
  <tr>
    <td class="call"><div class="halfBar right dashed"><img src="head.png" class="head-left"></div><div class="lifeline"></div></td>
    <td class="call"><div class="fullBar dashed"></div></td>
    <td class="call"><div class="halfBar left dashed"></div><div class="lifeline"></div></td>
    <td class="call"></td>
    <td class="call"><div class="lifeline"></div></td>
  </tr>
  <!--Vspace & Caption-->
  <tr>
    <td class="vspace"><div class="lifeline"></div></td>
    <td></td>
    <td class="vspace"><div class="lifeline"></div></td>
    <td></td>
    <td class="vspace"><div class="lifeline"></div></td>
  </tr>
</table>

</body>
</html>

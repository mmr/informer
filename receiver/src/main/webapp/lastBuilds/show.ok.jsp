<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://b1n.org/receiver" prefix="r" %>
<html>
<head>
<title>Build Stats</title>
<link rel='stylesheet' href='css/build.css' />
</head>
<body>
<h1><a href="lastBuilds.show.logic">Últimos Builds!</a></h1>
<hr />
<p align="center">
<table class="buildsByHour">
  <tr>
    <th>Hora</th>
    <th>Usuário</ht>
    <th>Projeto</ht>
    <th>Tests</th>
    <th>Deploy</ht>
    <th>Tempo</ht>
  </tr>

<c:forEach var="e" items="${buildsByHour}">
  <jsp:useBean id="e" type="java.util.Map.Entry" />
  <tr>
    <td class="hour" rowspan="<%= ((List) e.getValue()).size() + 1 %>">
      ${e.key}
    </td>
  </tr>

  <c:forEach var="b" items="${e.value}" varStatus="s">
    <r:tr build="${b}" status="${s}">
      <td class="user">
        <a href="lastBuilds.show.logic?userId=${b.user.id}"
        >${b.user.userName}</a>@<a href="lastBuilds.show.logic?hostId=${b.host.id}"
        >${b.host.hostName}</a>
      </td>

      <td class="project">
        <a href="lastBuilds.show.logic?projectId=${b.project.id}">
          ${b.project.artifactId} ${b.project.version}
        </a>
      </td>

      <td class="tests">
        <a href="lastBuilds.show.logic?withTests=${b.withTests}">
          <r:bool value="${b.withTests}" />
        </a>
      </td>

      <td class="deploy">
        <a href="lastBuilds.show.logic?deploy=${b.deploy}">
          <r:bool value="${b.deploy}" />
        </a>
      </td>

      <td class="buildTime">
        <r:buildTime value="${b.buildTime}" />
      </td>
    </r:tr>
  </c:forEach>
</c:forEach>

</table>
</p>
</body>
</html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>${title}</title>
  </head>
  <body>
    <h1>${title}</h1>
    <h2>Summary for project '<em>${project.name}</em>'</h2>
    
    <#if report.errors?hasContent>
      The project <span style="background-color: #FFAAAA"><strong>has formal errors</strong></span>.
      <#if report.warnings?hasContent>
        and has some <strong>warnings</strong>.
      </#if>
    <#else>
      <#if report.warnings?hasContent>
        The project <span style="background-color: #EEEE88"><strong>is formally valid</strong></span>
        but has some <strong>warnings</strong>.
      <#else>
        The project <span style="background-color: #00FF00"><strong>is formally valid</strong></span>
      </#if>
    </#if>
    
    <h3>${report.errors?size} errors </h3>
    
    <#if report.errors?hasContent>
      Note: the ordinal of the row is 0..N-1
    </#if>
    
    <table border="1">
      <#list report.errors as error>
        <tr><td bgcolor="#F0F0F0">
          ${error} 
        </td></tr>
      </#list>
    </table>
    
    <h3>${report.warnings?size} warnings </h3>
    
    <#if report.warnings?hasContent>
      Note: the ordinal of the row is 0..N-1
    </#if>
	
    <table border="1">
      <#list report.warnings as warning>
        <tr><td bgcolor="#F0F0F0">
          ${warning}
        </td></tr>
      </#list>
    </table>
	
  </body>
</html>
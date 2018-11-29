<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>${title}</title>
  </head>
  <body>
    <h1>${title}</h1>
    <h2>Overall status for project '<em>${project.name}</em>'</h2>
    
    <#if report.errors?hasContent>
      The project <strong>has formal errors</strong>.
      <#if report.warnings?hasContent>
        and has some <strong>warnings</strong>.
      </#if>
    <#else>
      The project is <strong>formally valid</strong>
      <#if report.warnings?hasContent>
        but has some <strong>warnings</strong>.
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
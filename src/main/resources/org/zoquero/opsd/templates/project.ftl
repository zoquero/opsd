<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>${title}</title>
  </head>
  <body>
    <h1>${title}</h1>
    <p>Article name = <b>${project.name}</b></p>
    <p>Article contents:</p>

    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre>
        
Project called '''${project.name}''', from responsible '''${project.responsible.name}'''.

== Description ==
${project.description}

${project.moreInfo}

== Dates ==
It was enabled in '''${projectDateIn}'''

It was closed in '''${projectDateOut}'''

== Dependencies ==
${project.dependencies}

== Recovery procedure ==
${project.recoveryProcedure}

== Roles ==

<#list roles as role>
=== ${role.name} ===
  <p>role name = ${role.name} , role description = ${role.description} </p> 
</#list>

== Systems ==

<#list systems as system>
=== ${system.name} ===
  <p>system name = ${system.name}
  <#if system.os?hasContent>
    system os = ${system.os} </p> 
  </#if>
</#list>

== MonitoredHosts ==

<#list monitoredHosts as monitoredHost>
=== ${monitoredHost.name} ===
  <p>monitoredHost name = ${monitoredHost.name} , monitoredHost IP = ${monitoredHost.ip} </p> 
</#list>

        </pre>
      </td></tr>
    </table>

  </body>
</html>
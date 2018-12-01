<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>${title}</title>
  </head>
  <body>
    <h1>${title}</h1>
    Contents:
    <ul>
      <li> <a href="#gd">Generated documentation</a> </li>
      <li> * <a href="#ap">Article for project '<em>${project.name}</em>'</a> </li>
      <li> * <a href="#asp">Article for service procedures</a> </li>
      <li> * ...</li>
      
      <li> <a href="#monitoring">Script to setup monitoring</a> </li>
      <li> * <a href="#addHosts">Script to setup monitored hosts</a> </li>
      <li> * <a href="#addServices">Script to setup monitored services</a> </li>
      <li> * ...</li>
    </ul>
    
    <h2 id="gd">Generated documentation</h2>
    <h3 id="ap">Article for project '<em>${project.name}</em>'</h3>
    <p>Article name = '<span style="background-color: #F0F0F0"><em>${project.name}</em></span>'</p>
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

== RoleServices SIMPLE ==

<#list roleServices as roleService>
=== ${roleService.name} ===
  <p>roleService name = ${roleService.name} , roleService description = ${roleService.description} </p> 
</#list>

== RoleServices by map ==

<#list role2servicesMap as role, roleServices> 
=== Role '${role.name}' ===
<#list roleServices as aService>
==== Service '${aService.name}' ====
</#list>
</#list> 

        </pre>
      </td></tr>
    </table>

    <h3 id="asp">Article for service procedures</h3>
    <p>Article name = '<span style="background-color: #F0F0F0"><em>Procedures for ${project.name}</em></span>'</p>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre>
Procedures for the services of the project '''${project.name}''':
== Services ==
=== ... ===
        </pre>
      </td></tr>
    </table>
    
    <br/><hr/><br/>
    <h2 id="monitoring">Script to setup monitoring</h2>
    <h3 id="addHosts">Script to setup monitored hosts</h3>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre>

<#list monitoredHosts as monitoredHost>
addHost.pl -host ${monitoredHost.name} -ip ${monitoredHost.ip} -... STILL UNFINISHED
</#list>
        </pre>
      </td></tr>
    </table>
    
    <h3 id="addServices">Script to setup monitored services</h3>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre>
addService.pl ... -... STILL UNFINISHED
addService.pl ... -... STILL UNFINISHED
addService.pl ... -... STILL UNFINISHED
        </pre>
      </td></tr>
    </table>
    
    <br/>
  </body>
</html>
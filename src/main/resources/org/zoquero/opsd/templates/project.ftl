<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>${title}</title>
  </head>
  <body>
    <h1>${title}</h1>
    Contents:
    <ul>
      <li> <a href="#validation">1) Validation of the data</a> </li>
      <li> <a href="#gd">2) Generated documentation</a> </li>
      <li> * <a href="#ap">Article for project '<em>${project.name}</em>'</a> </li>
      <li> * <a href="#asp">Article for service procedures</a> </li>
      <li> * ...</li>
      
      <li> <a href="#monitoring">3) Script to setup monitoring</a> </li>
      <li> * <a href="#addHosts">Script to setup monitored hosts</a> </li>
      <li> * <a href="#addServices">Script to setup monitored services</a> </li>
      <li> * ...</li>
    </ul>
    
    <h2 id="validation">1) Validation of the data</h2>

    <p>
    <#if report.errors?hasContent>
      The project <span style="background-color: #FFAAAA"><strong>has formal errors</strong></span>
      <#if report.warnings?hasContent>
        and has some <strong>warnings</strong>
      </#if>
      . It must be corrected.
    <#else>
      <#if report.warnings?hasContent>
        The project <span style="background-color: #EEEE88"><strong>is formally valid</strong></span>
        but has some <strong>warnings</strong>.
      <#else>
        The project <span style="background-color: #00FF00"><strong>is formally valid</strong></span>
      </#if>
    </#if>:
    </p>
    
    <h3>${report.errors?size} errors </h3>
    
    <#if report.errors?hasContent>
      Note: the ordinal of the row is 0..N-1
    </#if>
    
    <table border="1">
      <#list report.errors as error>
        <tr><td bgcolor="#FFF0F0">
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
        <tr><td bgcolor="#FFFFCC">
          ${warning}
        </td></tr>
      </#list>
    </table>
    
    <br/><hr/><br/>
    <h2 id="gd">2) Generated documentation</h2>
    <p>You can test it in <a href="https://www.mediawiki.org/w/index.php?title=Project:Sandbox&action=edit">MediaWiki's sandbox</a>. Be carefull if it contains sensible information.</p>
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
  <p>monitoredHost name = ${monitoredHost.name} , monitoredHost IP = ${monitoredHost.ip} , role = ${monitoredHost.role.name} </p> 
</#list>

== Services for roles ==

<#list role2servicesMap as role, roleServices> 
=== Role '${role.name}' ===

  <#list roleServices as aRoleService>
==== Service '${aRoleService.name}' ====
  <p>aRoleService name = ${aRoleService.name} , aRoleService description = ${aRoleService.description} </p>
  
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
    <h2 id="monitoring">3) Script to setup monitoring</h2>
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
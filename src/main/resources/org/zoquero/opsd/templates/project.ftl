<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>${title}</title>
  </head>
  <body>
    <h1>${title}</h1>
    Contents:
    <ul>
      <li> <a href="#log">0) Output log</a> </li>
      <li> <a href="#validation">1) Validation of the data</a> </li>
      <li> <a href="#gd">2) Generated documentation</a> </li>
      <li> * <a href="#ap">Article for project '<em>${project.name}</em>'</a> </li>
      <li> * <a href="#asp">Article for service procedures</a> </li>
      <li> * <a href="#requests">Article for requests (requestable tasks)</a> </li>
      <li> * <a href="#periodictasks">Article for periodic tasks</a> </li>
      <li> * <a href="#filepolicies">Article for file policies</a> </li>
      <li> * <a href="#systems">Articles for systems (assets)</a> </li>
      <li> * <a href="#monitoredhosts">Articles for MonitoredHosts</a> </li>
      <li> * <a href="#roles">Articles for Roles</a> </li>
      <li> * ...</li>
      
      <li> <a href="#monitoring">3) Script to setup monitoring</a> </li>
      <li> * <a href="#addHosts">Script to setup monitored hosts</a> </li>
      <li> * <a href="#addServices">Script to setup monitored services</a> </li>
      <li> * ...</li>
    </ul>
    
    <h2 id="log">0) Output log</h2>
    <p>Here's the <a href="./output.html">output log</a>.</p>
    
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

__FORCETOC__
 
<#if project.responsible?hasContent>
Project called '''${project.name}''', from responsible '''${project.responsible.name}'''.
<#else>
Project called '''${project.name}''', from responsible '''ERROR, MISSING RESPONSIBLE NAME'''
</#if>

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
  <#if hostServices?hasContent>
    <#list roleServices as aService>
==== Service '${aService.name}' ====
  <p>aService name = ${aService.name} , aService description = ${aService.description} </p>
    </#list>
  <#else>
  <p>Role without services</p>
  </#if>
</#list>

== Services for monitoredHosts ==

<#list host2servicesMap as host, hostServices> 
=== MonitoredHost '${host.name}' ===
  <#if hostServices?hasContent>
    <#list hostServices as aService>
==== Service '${aService.name}' ====
  <p>aService name = ${aService.name} , aService description = ${aService.description} </p>
    </#list>
  <#else>
  <p>Host without direct services, maybe by role</p>
  </#if>
</#list>

== Effective Services for monitoredHosts (mixing direct services + role services) ==
<#list hosteffectiveServicesMap as host, hostServices> 
=== MonitoredHost '${host.name}' ===
  <#if hostServices?hasContent>
    <#list hostServices as aService>
==== Service '${aService.name}' ====
  <p>aService name = ${aService.name} , aService description = ${aService.description} </p>
    </#list>
  <#else>
  <p><strong>Host without direct services</strong></p>
  </#if>
</#list>

        </pre>
      </td></tr>
    </table>

    <h3 id="asp">Article for service procedures</h3>
    <p>Article name = '<span style="background-color: #F0F0F0"><em>Procedures for ${project.name}</em></span>'</p>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre>
__FORCETOC__
Procedures for the services of the project [[${project.name}]]:
== Effective Services for monitoredHosts (mixing direct services + role services) ==
<#list hosteffectiveServicesMap as host, hostServices> 
=== MonitoredHost '${host.name}' ===
  <#if hostServices?hasContent>
    <#list hostServices as aService>
==== Service '${aService.name}' ====
aService name = ${aService.name} , aService description = ${aService.description}

Procedure = <#if aService.procedure?hasContent> ${aService.procedure} <#else> null (scale it up) </#if>

    </#list>
  <#else>
  <p><strong>Host without direct services</strong></p>
  </#if>
</#list>

        </pre>
      </td></tr>
    </table>
    
    <br/><hr/><br/>
    
    <h3 id="requests">Article for requests (requestable tasks)</h3>
    <p>Article name = '<span style="background-color: #F0F0F0"><em>Requests for ${project.name}</em></span>'</p>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre>
__FORCETOC__
Tasks that can be requested for the project '''[[${project.name}]]'''
== Requests ==

<#list requests as request>
=== ${request.name} ===
  Request name: ${request.name}
  Can be requested by: <#if request.authorized?hasContent> ${request.authorized} <#else> Error (null) </#if>  
  Procedure: <#if request.procedure?hasContent> ${request.procedure} <#else> Error (null) </#if>
  ScaleTo: <#if request.scaleTo?hasContent> ${request.scaleTo} <#else> Error (null) </#if>
</#list>
            </pre>
      </td></tr>
    </table>
    
    <br/><hr/><br/>
    
    <h3 id="periodictasks">Article for periodic tasks</h3>
    <p>Article name = '<span style="background-color: #F0F0F0"><em>Periodic tasks for ${project.name}</em></span>'</p>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre>
__FORCETOC__
Periodic tasks for the project [[${project.name}]]:
... PENDING
        </pre>
      </td></tr>
    </table>
    
    <br/><hr/><br/>
    
    <h3 id="filepolicies">Article for file policies</h3>
    <p>Article name = '<span style="background-color: #F0F0F0"><em>File policies for ${project.name}</em></span>'</p>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre>
__FORCETOC__
File policies for the project [[${project.name}]]:
... PENDING
        </pre>
      </td></tr>
    </table>
    
    <br/><hr/><br/>
    
    <h3 id="systems">Articles for Systems (assets)</h3>
    <p>Article name = '<span style="background-color: #F0F0F0"><em>Systems for ${project.name}</em></span>'</p>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre>
__FORCETOC__
Systems for the project [[${project.name}]]:
... PENDING
        </pre>
      </td></tr>
    </table>
    
    <br/><hr/><br/>

    <h3 id="monitoredhosts">Article for MonitoredHosts</h3>
    <p>Article name = '<span style="background-color: #F0F0F0"><em>MonitoredHosts for ${project.name}</em></span>'</p>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre>
__FORCETOC__
MonitoredHosts for the project [[${project.name}]]:
... PENDING
        </pre>
      </td></tr>
    </table>
    
    <br/><hr/><br/>
    
    <h3 id="roles">Article for Roles</h3>
    <p>Article name = '<span style="background-color: #F0F0F0"><em>Roles for ${project.name}</em></span>'</p>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre>
__FORCETOC__
Roles for the project [[${project.name}]]:
... PENDING
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
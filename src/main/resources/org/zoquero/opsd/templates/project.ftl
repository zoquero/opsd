<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Validation of the project and generation of monitoring and documentation</title>
    <style>
      pre {
        overflow: auto;
      }
    </style>
  </head>
  <body>
    <h1>${title}</h1>
    Contents:
    
    <table border="1" width="95%">
      <tr><td bgcolor="#F5F5F0">
        <ul>
          <li> <a href="#log">0) Output log</a> </li>
          <li> <a href="#validation">1) Validation Project data</a> </li>
          <li> <a href="#gd">2) Generated documentation</a> </li>
          <ul>
            <li> <a href="#ap">Article for project '<em>${opsdProject.name}</em>'</a> </li>
            <li> <a href="#asp">Article for service procedures</a> </li>
            <li> <a href="#requests">Article for requests (requestable tasks)</a> </li>
            <li> <a href="#periodictasks">Article for periodic tasks</a> </li>
            <li> <a href="#filepolicies">Article for file policies</a> </li>
<#--
  -- We'll embed system's body in project's body
  --
            <li> <a href="#systems">Articles for systems (assets)</a> </li>
-->
            <li> <a href="#monitoredhosts">Articles for MonitoredHosts</a> </li>
          </ul>
          <li> <a href="#monitoring">3) Script to setup monitoring</a> </li>
          <ul>
            <li> <a href="#addHosts">Script to setup monitored hosts</a> </li>
            <li> <a href="#addServices">Script to setup monitored services</a> </li>
          </ul>
          <li> <a href="#serv_tmplt_def">4) Service Template definitions</a> </li>
        </ul>
      </td></tr>
    </table>

    <br/><hr/><br/>
    
    <h2 id="log">0) Output log</h2>
    <p>Here's the <a href="./output.html">output log</a>.</p>

    <br/><hr/><br/>


    <h2 id="validation">1) Validation Project data</h2>

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

    <h3 id="ap">Article for project '<em>${opsdProject.name}</em>'</h3>
    <p>Body for the article with name: '<span style="background-color: #F0F0F0"><em>${opsdProject.name}</em></span>' and URL: <a href='${wikiUrlBase}/${opsdProject.name}'>${wikiUrlBase}/${opsdProject.name}</a></p>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>

__FORCETOC__

Documentation for the project '<em>${opsdProject.name}</em>' generated using '[https://github.com/zoquero/opsd Operations Descriptor]' (${mavenProjectArtifactId}-${mavenProjectVersion}) on ${genDate} with ${report.errors?size} errors and ${report.warnings?size} warnings

== Project description ==

${wikiProject}

== Roles ==

<#list roles2wiki as role, roleWiki> 
=== Role '${role.name}' ===

${roleWiki}

</#list>

<#--
  -- It will not be used, it's a flat list.
  -- we rather show it in a environment > role > system fashion
  --
== Systems ==

<#list systems2wiki as system, systemStr>
=== ${system.name} ===
${systemStr}

</#list>
-->

<#--
  --
  -- Commented out
  -- To reduce the number of articles
  -- we'll embed the Systems (assets) body
  -- in Project's body
  --
== Systems by environment and role ==

<#list env2role2systems as env, role2systems> 
=== Environment '${env}' ===

  <#list role2systems as role, systems> 
==== Role '${role.name}' ====

    <#list systems as system>
* [[${assetArticleNamePrefix}${system.name}|${system.name}]]
    </#list>
    
  </#list>
</#list>
-->

== Systems by environment and role ==

<#list env2role2systemsEmbeddedWiki as env, role2systems> 
=== Environment '${env}' ===

  <#list role2systems as role, systems> 
==== Role '${role.name}' ====

    <#list systems as system>
${system}

    </#list>
    
  </#list>
</#list>


== MonitoredHosts by environment and role ==

<#list env2role2monitoredHosts as env, role2monitoredHosts> 
=== Environment '${env}' ===

  <#list role2monitoredHosts as role, monitoredHosts> 
==== Role '${role.name}' ====

    <#list monitoredHosts as monitoredHost>
* [[${monitoredHost.name}]]
    </#list>
    
  </#list>
</#list>

== Services and its procedures ==

Article for service procedures: [[${opsdProject.name}. Procedures for incidents]]

<#--
  -- We'll remove it in a future code cleanup, by now it will help here
  --

== Services for roles (legacy, will be deleted) ==

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

== Services for monitoredHosts (legacy, will be deleted) ==

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

== Effective Services for monitoredHosts (mixing direct services + role services) (LEGACY, moved to self-article) ==
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

-->

== Requestable tasks and its procedures ==

Article containing the procedures for requestable tasks: [[${opsdProject.name}. Procedures for incidents]]

== Periodic tasks and its procedures ==

Article containing the procedures periodic tasks: [[${opsdProject.name}. Periodic tasks]]

== File policies ==

Article containing the aging policies to apply to files: [[${opsdProject.name}. File policies]]

        </code></pre>
      </td></tr>
    </table>

    <br/><hr/><br/>

    <h3 id="asp">Article for service procedures</h3>
    <p>Body for the article with name: '<span style="background-color: #F0F0F0"><em>${opsdProject.name}. Procedures for incidents</em></span>' and URL: <a href='${wikiUrlBase}/${opsdProject.name}. Procedures for incidents'>${wikiUrlBase}/${opsdProject.name}. Procedures for incidents</a></p>
    
<#--
  -- We'll remove it in a future code cleanup, by now it will help here
  --

    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>
__FORCETOC__
Here are the procedures for the services of the project [[${opsdProject.name}]], grouped by MonitoredHost:
== Effective Services for monitoredHosts (mixing direct services + role services) ==
<#list hosteffectiveServicesMap as host, hostServices> 
=== MonitoredHost '${host.name}' ===

  <#if hostServices?hasContent>
  
    <#list hostServices as aService>
    
==== Service '${aService.name}' ====
aService name = ${aService.name} , aService description = ${aService.description}

Procedure = <#if aService.procedure?hasContent> ${aService.procedure} <#else> null (scale it up) </#if>

DOESN'T WORK, CAN'T INTERPOLATE USING VARIABLES AS INDEXES, JUST STATIC CONTENT
{effectiveService2wikiMap[aService]}

    </#list>
  <#else>
  
  Host without direct services
  
  </#if>
</#list>

        </code></pre>
      </td></tr>
    </table>

-->
    
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>

__FORCETOC__
Here are the procedures for the services of the project [[${opsdProject.name}]], grouping together for each host both the explicit services for each MonitoredHost and the services for its roles:

<#list host2effectiveServiceWikiVOMap as host, hostServiceWikis> 
=== ${host.name} ===
  <#if hostServiceWikis?hasContent>
Procedures for the services of the host '''[[${host.name}]]''':

    <#list hostServiceWikis as aServiceWikiVO>
==== Service ' ''${aServiceWikiVO.service.name}'' ' ====

${aServiceWikiVO.wiki}
  
    </#list>
  <#else>
    <strong>Host without services</strong>
    
  </#if>
&lt;hr/>
</#list>

        </code></pre>
      </td></tr>
    </table>
    
    <br/><hr/><br/>
    
    <h3 id="requests">Article for requests (requestable tasks)</h3>
    <p>Body for the article with name: '<span style="background-color: #F0F0F0"><em>${opsdProject.name}. Procedures for petitions</em></span>' and URL: <a href='${wikiUrlBase}/${opsdProject.name}. Procedures for petitions'>${wikiUrlBase}/${opsdProject.name}. Procedures for petitions</a></p>
    
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>
__FORCETOC__
Tasks that can be requested (hopefully by ticketing) for the project '''[[${opsdProject.name}]]''':

<#list requestVOs as aRequestVO> 
== Request ' ''${aRequestVO.request.name}'' ' ==

${aRequestVO.wiki}
  
</#list>
        </code></pre>
      </td></tr>
    </table>
    
    <br/><hr/><br/>
    
    <h3 id="periodictasks">Article for periodic tasks</h3>
    <p>Body for the article with name: '<span style="background-color: #F0F0F0"><em>${opsdProject.name}. Periodic tasks</em></span>' and URL: <a href='${wikiUrlBase}/${opsdProject.name}. Periodic tasks'>${wikiUrlBase}/${opsdProject.name}. Periodic tasks</a></p>
    
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>
__FORCETOC__
Periodic tasks for the project '''[[${opsdProject.name}]]'''

<#list periodicTaskVOs as aPeriodicTaskVO> 
== Periodic task '${aPeriodicTaskVO.periodicTask.name}' ==

${aPeriodicTaskVO.wiki}
  
</#list>
        </code></pre>
      </td></tr>
    </table>
    
    <br/><hr/><br/>
    
    <h3 id="filepolicies">Article for file policies</h3>
    <p>Body for the article with name: '<span style="background-color: #F0F0F0"><em>${opsdProject.name}. File policies</em></span>' and URL: <a href='${wikiUrlBase}/${opsdProject.name}. File policies'>${wikiUrlBase}/${opsdProject.name}. File policies</a></p>

<#list wikiFilePolicies as filePolicy, filePolicyWiki> 

A file policy for 
  <#if filePolicy.system?hasContent>
the system '${filePolicy.system.name}'
  <#else>
the system (none)
  </#if>
  <#if filePolicy.role?hasContent>
and all the hosts belonging to the role '${filePolicy.role.name}'
  <#else>
and all the hosts belonging to the role (none)
  </#if>:

    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>
${filePolicyWiki}
        </code></pre>
      </td></tr>
    </table>

</#list>
    
    <br/><hr/><br/>

<#--
  -- Finally we'll embed Systems in Project's article
  --    
    <h3 id="systems">Articles for Systems (assets)</h3>
    
<#list systems2wiki as system, systemStr>
    <h4>Article for System '<span style="background-color: #F0F0F0"><em>${system.name}</em></span>'</h4>
    <p>Body for the article with name: '<span style="background-color: #F0F0F0"><em>${assetArticleNamePrefix}${system.name}</em></span>' and URL: <a href='${wikiUrlBase}/${assetArticleNamePrefix}${system.name}'>${wikiUrlBase}/${assetArticleNamePrefix}${system.name}</a></p> 
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>
${systemStr}
        </code></pre>
      </td></tr>
    </table>

</#list>
    
    <br/><hr/><br/>
-->

    <h3 id="monitoredhosts">Articles for MonitoredHosts</h3>

<#list monitoredHosts2wiki as monitoredHost, monitoredHostStr>
    <h4>Article for MonitoredHost '<span style="background-color: #F0F0F0"><em>${monitoredHost.name}</em></span>'</h4>
    <p>Body for the article with name: '<span style="background-color: #F0F0F0"><em>${monitoredHost.name}</em></span>' and URL: <a href='${wikiUrlBase}/${monitoredHost.name}'>${wikiUrlBase}/${monitoredHost.name}</a></p> 
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>
${monitoredHostStr}
        </code></pre>
      </td></tr>
    </table>

</#list>

    <br/><hr/><br/>
    
    <h2 id="monitoring">3) Script to setup monitoring</h2>
    <h3 id="addHosts">Script to setup monitored hosts</h3>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>

# Delete host commands:
<#list monitoredHost2script as monitoredHost, monitoringHostCommands> 
${monitoringHostCommands.delHostCommand}
</#list>

# Add host commands:
<#list monitoredHost2script as monitoredHost, monitoringHostCommands> 
${monitoringHostCommands.addHostCommand}
</#list>

        </code></pre>
      </td></tr>
    </table>
    
    <h3 id="addServices">Script to setup monitored services</h3>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>
        
<#list monitoredEffectiveHostServiceCommands as hostService, monitoredServiceCommands>
${monitoredServiceCommands.addServiceCommand}
</#list>

        </code></pre>
      </td></tr>
    </table>
    
   <h2 id="serv_tmplt_def">4) Service Template definitions </h2>

<p>General attributes:</p>

    <table border="1">
      <tr>
        <td bgcolor="#F5F5F5">Service Name</td>
        <td bgcolor="#C0C0C0">NRPE?</td>
        <td bgcolor="#C0C0C0">Default service name</td>
        <td bgcolor="#C0C0C0">Description</td>
      </tr>
<#list serviceTemplates as serviceTemplate>
      <tr>
        <td bgcolor="#F0F0F0"> <strong>${serviceTemplate.name}</strong> </td>
        <td bgcolor="#F0F0F0"> ${serviceTemplate.nrpe?string} </td>
        <td bgcolor="#F0F0F0"> ${serviceTemplate.defaultName} </td>
        <td bgcolor="#F0F0F0"> ${serviceTemplate.description} </td>
      </tr>
</#list>
    </table>

<p>Macros (options for Service Templates):</p>

    <table border="1">
      <tr>
        <td bgcolor="#F0F0F0"></td>
<#list 1..numMacros as i>
        <td bgcolor="#D0D0D0" colspan=3 align="center">macro#${i}</td>
</#list>
      </tr>
      <tr>
        <td bgcolor="#C0C0C0">Service<br/>Name</td>
<#list 1..numMacros as i>
          <td bgcolor="#D0D0D0">Name</td>
          <td bgcolor="#DBDBDB">Description</td>
          <td bgcolor="#EAEAEA">Default<br/>value</td>
</#list>
      </tr>
<#list serviceTemplates as serviceTemplate>
      <tr>
        <td bgcolor="#F5F5F5"> ${serviceTemplate.name} </td>

  <#if serviceTemplate.macroDefinitions?hasContent>
    <#list serviceTemplate.macroDefinitions as mDef>
      <#if mDef?hasContent>
        <td bgcolor="#D0D0D0"> <strong>${mDef.name}</strong> </td>
        <td bgcolor="#DBDBDB"> ${mDef.description} </td>
        <td bgcolor="#EAEAEA"> ${mDef.defaultValue} </td>
      </#if>
    </#list>
  <#else>
  
    <#list 1..numMacros as i>
          <td bgcolor="#F0F0F0"> &nbsp </td>
          <td bgcolor="#F0F0F0"> &nbsp </td>
          <td bgcolor="#F0F0F0"> &nbsp </td>
    </#list>
  
  </#if>
      </tr>
</#list>
    </table>

   <br/>
  </body>
</html>

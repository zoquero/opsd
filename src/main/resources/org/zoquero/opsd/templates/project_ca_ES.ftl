<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Validació del projecte i generació de monitoratge i documentació</title>
    <style>
      pre {
        overflow: auto;
      }
    </style>
  </head>
  <body>
    <h1>Validació del projecte i generació de monitoratge i documentació</h1>
    Continguts:
    
    <table border="1" width="95%">
      <tr><td bgcolor="#F5F5F0">
        <ul>
          <li> <a href="#log">0) Log de sortida</a> </li>
          <li> <a href="#validation">1) Validació de les dades del Project</a> </li>
          <li> <a href="#gd">2) Documentació generada</a> </li>
          <ul>
            <li> <a href="#ap">Article del projecte '<em>${opsdProject.name}</em>'</a> </li>
            <li> <a href="#asp">Article dels procediments pels serveis</a> </li>
            <li> <a href="#requests">Article de les peticions sol·licitables</a> </li>
            <li> <a href="#periodictasks">Article de les tasques periòdiques</a> </li>
            <li> <a href="#filepolicies">Article de les polítiques per a fitxers</a> </li>
<#--
  -- We'll embed system's body in project's body
  --
            <li> <a href="#systems">Articles dels sistemes (assets)</a> </li>
-->
            <li> <a href="#monitoredhosts">Articles dels HostsMonitorats</a> </li>
          </ul>
          <li> <a href="#monitoring">3) Script per a establir el monitoratge</a> </li>
          <ul>
            <li> <a href="#addHosts">Script per a establir el monitoratge dels hosts</a> </li>
            <li> <a href="#addServices">Script per a establir el monitoratge dels serveis</a> </li>
          </ul>
          <li> <a href="#serv_tmplt_def">4) Definicions de les plantilles de serveis</a> </li>
        </ul>
      </td></tr>
    </table>

    <br/><hr/><br/>
    
    <h2 id="log">0) Log de sortida</h2>
    <p>Aquí està el <a href="./output.html">log de sortida</a>.</p>

    <br/><hr/><br/>


    <h2 id="validation">1) Validació de les dades del Project </h2>

    <p>
    <#if report.errors?hasContent>
      El projecte <span style="background-color: #FFAAAA"><strong>té errors formals</strong></span>
      <#if report.warnings?hasContent>
        i té algunes <strong>advertències</strong> (warnings)
      </#if>
      . S'ha de corregir.
    <#else>
      <#if report.warnings?hasContent>
        El projecte <span style="background-color: #EEEE88"><strong>és formalment vàlid</strong></span>
        but has some <strong>warnings</strong>.
      <#else>
        El projecte <span style="background-color: #00FF00"><strong>és formalment vàlid</strong></span>
      </#if>
    </#if>:
    </p>
    
    <h3>${report.errors?size} errors </h3>
    
    <#if report.errors?hasContent>
      Nota: L'ordinal de les files és 0..N-1
    </#if>
    
    <table border="1">
      <#list report.errors as error>
        <tr><td bgcolor="#FFF0F0">
          ${error}
        </td></tr>
      </#list>
    </table>
    
    <h3>${report.warnings?size} advertències (warnings) </h3>
    
    <#if report.warnings?hasContent>
      Nota: L'ordinal de les files és 0..N-1
    </#if>
	
    <table border="1">
      <#list report.warnings as warning>
        <tr><td bgcolor="#FFFFCC">
          ${warning}
        </td></tr>
      </#list>
    </table>
    
    <br/><hr/><br/>
    
    <h2 id="gd">2) Documentació generada</h2>
    <p>Pots provar-ho a <a href="https://www.mediawiki.org/w/index.php?title=Project:Sandbox&action=edit">MediaWiki's sandbox</a>, però tingues cura si conté informació sensible.</p>

    <h3 id="ap">Article del projecte '<em>${opsdProject.name}</em>'</h3>
    <p>Cos per l'article del projecte anomenat: '<span style="background-color: #F0F0F0"><em>${opsdProject.name}</em></span>' i amb URL: <a href='${wikiUrlBase}/${opsdProject.name}'>${wikiUrlBase}/${opsdProject.name}</a></p>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>

__FORCETOC__

Documentació pel projecte '<em>${opsdProject.name}</em>' generada utilitzant '[https://github.com/zoquero/opsd Operations Descriptor]' (${mavenProjectArtifactId}-${mavenProjectVersion}) a ${genDate} amb ${report.errors?size} errors i ${report.warnings?size} advertències (warnings)

== Descripció del projecte ==

${wikiProject}

== Rols ==

<#list roles2wiki as role, roleWiki> 
=== Rol '' '${role.name}'' ' ===

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
== Sistemes per entorn i rol ==

<#list env2role2systems as env, role2systems> 
=== Entorn '' '${env}' '' ===

  <#list role2systems as role, systems> 
==== Rol '' '${role.name}' '' ====

    <#list systems as system>
* [[${assetArticleNamePrefix}${system.name}|${system.name}]]
    </#list>
    
  </#list>
</#list>
-->

== Sistemes per entorn i rol ==

<#list env2role2systemsEmbeddedWiki as env, role2systems> 
=== Entorn '' '${env}' '' ===

  <#list role2systems as role, systems> 
==== Rol '' '${role.name}' '' ====

    <#list systems as system>
${system}

    </#list>
    
  </#list>
</#list>


== HostsMonitorats per entorn i rol ==

<#list env2role2monitoredHosts as env, role2monitoredHosts> 
=== Entorn '' '${env}' '' ===

  <#list role2monitoredHosts as role, monitoredHosts> 
==== Rol '' '${role.name}' '' ====

    <#list monitoredHosts as monitoredHost>
* [[${monitoredHost.name}]]
    </#list>
    
  </#list>
</#list>

== Serveis i els seus procediments ==

Article amb els procediments pels serveis: [[Procediments per ${opsdProject.name}]]

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

        </code></pre>
      </td></tr>
    </table>

    <br/><hr/><br/>

    <h3 id="asp">Article dels procediments pels serveis</h3>
    <p>Cos de l'article anomenat: '<span style="background-color: #F0F0F0"><em>Procediments per ${opsdProject.name}</em></span>' i URL: <a href='${wikiUrlBase}/Procediments per ${opsdProject.name}'>${wikiUrlBase}/Procediments per ${opsdProject.name}</a></p>
    
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
Aquests són els procediments pels serveis del projecte [[${opsdProject.name}]], agrupats per cada host, tant els serveis definits explícitament per al host, com els serveis definits pel rol al que el host pertany.

<#list host2effectiveServiceWikiVOMap as host, hostServiceWikis> 
=== ${host.name} ===
  <#if hostServiceWikis?hasContent>
Procediments pels serveis del host '''[[${host.name}]]''':

    <#list hostServiceWikis as aServiceWikiVO>
==== Servei ' ''${aServiceWikiVO.service.name}'' ' ====

${aServiceWikiVO.wiki}
  
    </#list>
  <#else>
    <strong>Host sense serveis</strong>
    
  </#if>
&lt;hr/>
</#list>

        </code></pre>
      </td></tr>
    </table>
    
    <br/><hr/><br/>
    
    <h3 id="requests">Article de les peticions sol·licitables</h3>
    <p>Cos de l'article anomenat: '<span style="background-color: #F0F0F0"><em>Peticions per ${opsdProject.name}</em></span>' i URL: <a href='${wikiUrlBase}/Peticions per ${opsdProject.name}'>${wikiUrlBase}/Peticions per ${opsdProject.name}</a></p>
    
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>
__FORCETOC__
Peticions que poden ser sol·licitades (tant de bo que via tiquet) pel projecte '''[[${opsdProject.name}]]''':

<#list requestVOs as aRequestVO> 
== Petició ' ''${aRequestVO.request.name}'' ' ==

${aRequestVO.wiki}
  
</#list>
        </code></pre>
      </td></tr>
    </table>
    
    <br/><hr/><br/>
    
    <h3 id="periodictasks">Article de les tasques periòdiques</h3>
    <p>Cos de l'article anomenat: '<span style="background-color: #F0F0F0"><em>Tasques periòdiques per ${opsdProject.name}</em></span>' i URL: <a href='${wikiUrlBase}/Tasques periòdiques per ${opsdProject.name}'>${wikiUrlBase}/Tasques periòdiques per ${opsdProject.name}</a></p>
    
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>
__FORCETOC__
Tasques periòdiques pel projecte '''[[${opsdProject.name}]]'''

<#list periodicTaskVOs as aPeriodicTaskVO> 
== Tasca periòdica '${aPeriodicTaskVO.periodicTask.name}' ==

${aPeriodicTaskVO.wiki}
  
</#list>
        </code></pre>
      </td></tr>
    </table>
    
    <br/><hr/><br/>
    
    <h3 id="filepolicies">Article de les polítiques per a fitxers</h3>
    <p>Cos de l'article anomenat: '<span style="background-color: #F0F0F0"><em>Polítiques de fitxers per ${opsdProject.name}</em></span>' i URL: <a href='${wikiUrlBase}/Polítiques de fitxers per ${opsdProject.name}'>${wikiUrlBase}/Polítiques de fitxers per ${opsdProject.name}</a></p>

<#list wikiFilePolicies as filePolicy, filePolicyWiki> 

Una política de fitxers
  <#if filePolicy.system?hasContent>
pel sistema '${filePolicy.system.name}'
  <#else>
pel sistema (cap en concret)
  </#if>
  <#if filePolicy.role?hasContent>
i tots els hosts que pertanyin al rol '${filePolicy.role.name}'
  <#else>
i tots els hosts que pertanyin al rol (cap en concret)
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
    <p>Body for the article with name: '<span style="background-color: #F0F0F0"><em>${assetArticleNamePrefix}${system.name}</em></span>' i URL: <a href='${wikiUrlBase}/${assetArticleNamePrefix}${system.name}'>${wikiUrlBase}/${assetArticleNamePrefix}${system.name}</a></p> 
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

    <h3 id="monitoredhosts">Articles dels HostsMonitorats</h3>

<#list monitoredHosts2wiki as monitoredHost, monitoredHostStr>
    <h4>Article pel HostMonitorat '<span style="background-color: #F0F0F0"><em>${monitoredHost.name}</em></span>'</h4>
    <p>Cos per l'article anomenat: '<span style="background-color: #F0F0F0"><em>${monitoredHost.name}</em></span>' i URL: <a href='${wikiUrlBase}/${monitoredHost.name}'>${wikiUrlBase}/${monitoredHost.name}</a></p> 
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>
${monitoredHostStr}
        </code></pre>
      </td></tr>
    </table>

</#list>

    <br/><hr/><br/>
    
    <h2 id="monitoring">3) Script per a establir el monitoratge</h2>
    <h3 id="addHosts">Script per a establir el monitoratge dels hosts:</h3>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>

# Comandes per esborrar hosts:
<#list monitoredHost2script as monitoredHost, monitoringHostCommands> 
${monitoringHostCommands.delHostCommand}
</#list>

# Comanes per afegir hosts:
<#list monitoredHost2script as monitoredHost, monitoringHostCommands> 
${monitoringHostCommands.addHostCommand}
</#list>

        </code></pre>
      </td></tr>
    </table>
    
    <h3 id="addServices">Script per a establir el monitoratge dels serveis:</h3>
    <table border="1">
      <tr><td bgcolor="#F0F0F0">
        <pre><code>
        
<#list monitoredEffectiveHostServiceCommands as hostService, monitoredServiceCommands>
${monitoredServiceCommands.addServiceCommand}
</#list>

        </code></pre>
      </td></tr>
    </table>
    
   <h2 id="serv_tmplt_def">4) Definicions de les plantilles de serveis </h2>

<p>Atributs generals:</p>

    <table border="1">
      <tr>
        <td bgcolor="#F5F5F5">Nom de servei</td>
        <td bgcolor="#C0C0C0">NRPE?</td>
        <td bgcolor="#C0C0C0">Nom de servei per defecte</td>
        <td bgcolor="#C0C0C0">Descripció</td>
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

<p>Macros (opcions per les Plantilles de Servei):</p>

    <table border="1">
      <tr>
        <td bgcolor="#F0F0F0"></td>
<#list 1..numMacros as i>
        <td bgcolor="#D0D0D0" colspan=3 align="center">macro#${i}</td>
</#list>
      </tr>
      <tr>
        <td bgcolor="#C0C0C0">Nom de<br/>Servei</td>
<#list 1..numMacros as i>
          <td bgcolor="#D0D0D0">Nom</td>
          <td bgcolor="#DBDBDB">Descripció</td>
          <td bgcolor="#EAEAEA">Valor per<br/>defecte</td>
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

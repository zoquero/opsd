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

        </pre>
      </td></tr>
    </table>

  </body>
</html>
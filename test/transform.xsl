<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns="http://www.w3.org/1999/xhtml"
  xmlns:h="http://www.w3.org/1999/xhtml"
  xmlns:c="http://cnx.rice.edu/cnxml"
  xmlns:m="http://www.w3.org/1998/Math/MathML"
  xmlns:mml="http://www.w3.org/1998/Math/MathML"
  xmlns:md="http://cnx.rice.edu/mdml"
  xmlns:qml="http://cnx.rice.edu/qml/1.0"
  xmlns:mod="http://cnx.rice.edu/#moduleIds"
  xmlns:bib="http://bibtexml.sf.net/"
  xmlns:data="http://www.w3.org/TR/html5/dom.html#custom-data-attribute"
  xmlns:epub="http://www.idpf.org/2007/ops"
  exclude-result-prefixes="m mml"

  >

<xsl:output encoding="ASCII" method="xml" />


<xsl:template match="h:body">
  <xsl:copy>
    <p>adfksjfskdf</p>
    <xsl:apply-templates select="node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="@*|node()">
    <xsl:copy>
        <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
</xsl:template>

</xsl:stylesheet>
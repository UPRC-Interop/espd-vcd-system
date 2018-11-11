<?xml version="1.0" encoding="UTF-8"?>

<!-- Beinhaltet alle Formatierungsregeln fuer ein Kapitel -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

    <xsl:output method="xml" version="1.0" doctype-public="-//W3C//DTD XHTML 1.1//EN"
                doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" indent="yes" encoding="UTF-8"/>

    <!--
        * Chapters Template
        *
        * @param level
        *            Integer, der anzeigt um welches Level es sich handelt (Nullable)
        *            null/1 = Kein Level
        *            2 = level-two etc.
        * @param chapter-number
        *            Kapitelnummer (Nullable)
        * @param chapter-headline
        *            Kopfzeile
        * @param chapter-content
        *            Inhalt des Kapitels
    -->
    <xsl:template name="chapter">
        <xsl:param name="level"/>
        <xsl:param name="chapter-number"/>
        <xsl:param name="chapter-headline"/>
        <xsl:param name="chapter-content"/>

        <xsl:variable name="level-string">
            <xsl:call-template name="getLevelString">
                <xsl:with-param name="level" select="$level"/>
            </xsl:call-template>
        </xsl:variable>

        <xsl:variable name="chapter-string">
            <xsl:call-template name="getChapterString">
                <xsl:with-param name="chapter-number" select="$chapter-number"/>
            </xsl:call-template>
        </xsl:variable>

        <div>
            <xsl:attribute name="class">
                <xsl:text>chapter</xsl:text>
                <xsl:value-of select="$level-string"/>
                <xsl:value-of select="$chapter-string"/>
            </xsl:attribute>

            <xsl:if test="normalize-space($chapter-number) != ''">
                <div class="chapter-number">
                    <xsl:value-of select="$chapter-number"/>
                </div>
            </xsl:if>

            <xsl:if test="normalize-space($chapter-headline) != '' ">
                <div class="chapter-headline">
                    <xsl:copy-of select="$chapter-headline"/>
                </div>
            </xsl:if>
            <xsl:if test="normalize-space($chapter-content) != '' ">
                <div class="chapter-content">
                    <xsl:copy-of select="$chapter-content"/>
                </div>
            </xsl:if>
        </div>
    </xsl:template>

    <xsl:template name="getLevelString">
        <xsl:param name="level"/>
        <xsl:choose>
            <xsl:when test="$level = '2'">
                <xsl:text> level-two</xsl:text>
            </xsl:when>
            <xsl:when test="$level = '3'">
                <xsl:text> level-three</xsl:text>
            </xsl:when>
            <xsl:when test="$level = '4'">
                <xsl:text> level-four</xsl:text>
            </xsl:when>
            <xsl:otherwise/>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="getChapterString">
        <xsl:param name="chapter-number"/>
        <xsl:choose>
            <xsl:when test="normalize-space($chapter-number) = ''">
                <xsl:text> no-chapter-number</xsl:text>
            </xsl:when>
            <xsl:otherwise/>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="chapterOne">
        <xsl:param name="chapter-number"/>
        <xsl:param name="chapter-headline"/>
        <xsl:param name="chapter-content"/>

        <xsl:call-template name="chapter">
            <xsl:with-param name="level">1</xsl:with-param>
            <xsl:with-param name="chapter-number" select="$chapter-number"/>
            <xsl:with-param name="chapter-headline" select="$chapter-headline"/>
            <xsl:with-param name="chapter-content" select="$chapter-content"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="chapterTwo">
        <xsl:param name="chapter-number"/>
        <xsl:param name="chapter-headline"/>
        <xsl:param name="chapter-content"/>

        <xsl:call-template name="chapter">
            <xsl:with-param name="level">2</xsl:with-param>
            <xsl:with-param name="chapter-number" select="$chapter-number"/>
            <xsl:with-param name="chapter-headline" select="$chapter-headline"/>
            <xsl:with-param name="chapter-content" select="$chapter-content"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="chapterOneNoNumber">
        <xsl:param name="chapter-headline"/>
        <xsl:param name="chapter-content"/>

        <xsl:call-template name="chapter">
            <xsl:with-param name="level">1</xsl:with-param>
            <xsl:with-param name="chapter-number"/>
            <xsl:with-param name="chapter-headline" select="$chapter-headline"/>
            <xsl:with-param name="chapter-content" select="$chapter-content"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="chapterTwoNoNumber">
        <xsl:param name="chapter-headline"/>
        <xsl:param name="chapter-content"/>

        <xsl:call-template name="chapter">
            <xsl:with-param name="level">2</xsl:with-param>
            <xsl:with-param name="chapter-number"/>
            <xsl:with-param name="chapter-headline" select="$chapter-headline"/>
            <xsl:with-param name="chapter-content" select="$chapter-content"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="chapterThree">
        <xsl:param name="chapter-number"/>
        <xsl:param name="chapter-headline"/>
        <xsl:param name="chapter-content"/>

        <xsl:call-template name="chapter">
            <xsl:with-param name="level">3</xsl:with-param>
            <xsl:with-param name="chapter-number" select="$chapter-number"/>
            <xsl:with-param name="chapter-headline" select="$chapter-headline"/>
            <xsl:with-param name="chapter-content" select="$chapter-content"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="chapterFour">
        <xsl:param name="chapter-number"/>
        <xsl:param name="chapter-headline"/>
        <xsl:param name="chapter-content"/>

        <xsl:call-template name="chapter">
            <xsl:with-param name="level">4</xsl:with-param>
            <xsl:with-param name="chapter-number" select="$chapter-number"/>
            <xsl:with-param name="chapter-headline" select="$chapter-headline"/>
            <xsl:with-param name="chapter-content" select="$chapter-content"/>
        </xsl:call-template>
    </xsl:template>

</xsl:stylesheet>
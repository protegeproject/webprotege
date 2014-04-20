package edu.stanford.bmir.protege.web.server.renderer;

import edu.stanford.bmir.protege.web.server.render.ImageLinkRenderer;
import edu.stanford.bmir.protege.web.server.render.LinkInfo;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/04/2014
 */
public class ImageLinkRendererTestCase {


    private ImageLinkRenderer renderer;

    @Before
    public void setUp() throws Exception {
        renderer = new ImageLinkRenderer();
    }

    @Test
    public void shouldParseLinkWithPngExtensionAsImage() {
        verifyLink("png");
    }

    @Test
    public void shouldParseLinkWithJpgExtensionAsImage() {
        verifyLink("jpg");
    }

    @Test
    public void shouldParseLinkWithJpegExtensionAsImage() {
        verifyLink("jpeg");
    }

    @Test
    public void shouldParseLinkWithGifExtensionAsImage() {
        verifyLink("gif");
    }

    @Test
    public void shouldParseLinkWithSvgExtensionAsImage() {
        verifyLink("svg");
    }




    private void verifyLink(String extension) {
        verifyLink("http", extension);
        verifyLink("http", extension.toUpperCase());
        verifyLink("https", extension);
        verifyLink("https", extension.toUpperCase());
    }

    private void verifyLink(String scheme, String extension) {
        String link = scheme + "://other.com/img." + extension;
        assertThat(scheme + " and ." + extension + " is not renderable as link",
                renderer.isRenderableAsLink(link), is(true));
        LinkInfo linkInfo = renderer.renderLink(link);
        assertThat(scheme + " and ." + extension + " failed", linkInfo, is(not((LinkInfo) null)));
        assertThat(linkInfo.getLinkAddress(), is(equalTo(link)));
    }
}

package edu.stanford.bmir.protege.web.client.ui;

import com.gwtext.client.widgets.Panel;
import edu.stanford.bmir.protege.web.client.banner.BannerPresenter;
import edu.stanford.bmir.protege.web.client.banner.BannerView;

/**
 * The panel shown at the top of the display. It contains the documentation
 * links, the sign in/out links, and may contain other menus, etc.
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
@SuppressWarnings("deprecation")
public class TopPanel extends Panel {


    public TopPanel() {
        setCls("top-panel");
        // Inner panel to house links panel
        BannerPresenter bannerPresenter = new BannerPresenter();
        BannerView banner = bannerPresenter.getView();
        add(banner.asWidget());
    }
}

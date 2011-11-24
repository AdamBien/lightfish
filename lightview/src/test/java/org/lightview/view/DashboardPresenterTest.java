package org.lightview.view;

import org.junit.Before;
import org.junit.Test;
import org.lightview.service.SnapshotProvider;

import static org.mockito.Mockito.*;

/**
 * User: blog.adam-bien.com
 * Date: 21.11.11
 * Time: 18:08
 */
public class DashboardPresenterTest {

    private DashboardPresenter cut;

    @Before
    public void init() throws Exception {
        this.cut = new DashboardPresenter(){};
        this.cut.service = mock(SnapshotProvider.class);
    }

    @Test
    public void changeUri() throws Exception {
        this.cut.setUri("hugo");
    }
}

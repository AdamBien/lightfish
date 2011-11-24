package org.lightview.view;

import org.lightview.entity.Snapshot;

/**
 * User: blog.adam-bien.com
 * Date: 22.11.11
 * Time: 18:53
 */
public interface SnapshotListener {

    public void onSnapshotArrival(Snapshot snapshot);
}

package com.wmx.android.wrstar.utils.capturePhoto;

import java.io.File;

abstract class AlbumStorageDirFactory {
	public abstract File getAlbumStorageDir(String albumName);
}

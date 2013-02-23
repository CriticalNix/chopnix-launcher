package com.chopnix.data.events;

import com.chopnix.data.ModPack;

public interface ModPackListener {
	/*
	 * Fired by the ModPack Singleton once a modpack has been added. Beware its
	 * called for EVERY pack thats added!
	 */
	public void onModPackAdded(ModPack pack);
}

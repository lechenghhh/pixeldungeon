/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.watabou.pixeldungeon.items.keys;

import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;

public class SkeletonKey extends Key {

	{
		name = "skeleton key";
		image = ItemSpriteSheet.SKELETON_KEY;
	}

	@Override
	public String info() {
		return
			"这把钥匙看起来很严肃：它的头形状像头骨。也许它能打开一扇严肃的门。";
	}
}

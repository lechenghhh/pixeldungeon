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
package com.watabou.pixeldungeon.items.bags;

import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.plants.Plant;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;

public class SeedPouch extends Bag {

	{
		name = "种子袋";
		image = ItemSpriteSheet.POUCH;

		size = 8;
	}

	@Override
	public boolean grab( Item item ) {
		return item instanceof Plant.Seed;
	}

	@Override
	public int price() {
		return 50;
	}

	@Override
	public String info() {
		return
			"这个小天鹅绒袋允许你储存任何数量的种子。很方便。";
	}
}

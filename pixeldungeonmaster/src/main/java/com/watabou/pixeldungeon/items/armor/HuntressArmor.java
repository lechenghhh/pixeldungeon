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
package com.watabou.pixeldungeon.items.armor;

import java.util.HashMap;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.hero.HeroClass;
import com.watabou.pixeldungeon.actors.mobs.Mob;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.weapon.missiles.Shuriken;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.sprites.MissileSprite;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class HuntressArmor extends ClassArmor {

	private static final String TXT_NO_ENEMIES 		= "看不到敌人";
	private static final String TXT_NOT_HUNTRESS	= "Only 女猎手 can use this armor!";

	private static final String AC_SPECIAL = "SPECTRAL BLADES光谱叶片";

	{
		name = "女猎手斗篷 ";
	image = ItemSpriteSheet.ARMOR_HUNTRESS;
	}

	private HashMap<Callback, Mob> targets = new HashMap<Callback, Mob>();

	@Override
	public String special() {
		return AC_SPECIAL;
	}

	@Override
	public void doSpecial() {

		Item proto = new Shuriken();

		for (Mob mob : Dungeon.level.mobs) {
			if (Level.fieldOfView[mob.pos]) {

				Callback callback = new Callback() {
					@Override
					public void call() {
						curUser.attack( targets.get( this ) );
						targets.remove( this );
						if (targets.isEmpty()) {
							curUser.spendAndNext( curUser.attackDelay() );
						}
					}
				};

				((MissileSprite)curUser.sprite.parent.recycle( MissileSprite.class )).
					reset( curUser.pos, mob.pos, proto, callback );

				targets.put( callback, mob );
			}
		}

		if (targets.size() == 0) {
			GLog.w( TXT_NO_ENEMIES );
			return;
		}

		curUser.HP -= (curUser.HP / 3);

		curUser.sprite.zap( curUser.pos );
		curUser.busy();
	}

	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.heroClass == HeroClass.HUNTRESS) {
			return super.doEquip( hero );
		} else {
			GLog.w( TXT_NOT_HUNTRESS );
			return false;
		}
	}

	@Override
	public String desc() {
		return
			"一个穿着这种斗篷的女猎手可以制造出一个幽灵之刃的扇子。每一把利刃都会在女猎手的视野内瞄准一个敌人，根据她目前装备的近战武器造成伤害";
	}
}
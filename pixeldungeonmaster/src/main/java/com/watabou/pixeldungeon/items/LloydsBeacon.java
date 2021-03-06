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
package com.watabou.pixeldungeon.items;

import java.util.ArrayList;

import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.items.wands.WandOfBlink;
import com.watabou.pixeldungeon.levels.Level;
import com.watabou.pixeldungeon.scenes.InterlevelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;

public class LloydsBeacon extends Item {

	private static final String TXT_PREVENTING =
		"这个地方强大的魔法光环阻止你使用劳埃德灯塔!";

	private static final String TXT_CREATURES =
		"邻近生物的灵能光环不允许你使用劳埃德灯塔。";

	private static final String TXT_RETURN =
		"劳埃德灯塔已成功设置在您当前的位置，现在您可以随时返回这里。";

	private static final String TXT_INFO =
		"劳埃德灯塔是一个复杂的魔法装置，可以让你回到你已经去过的地方。";

	private static final String TXT_SET =
		"\n\n这个信标设置在像素地牢的 %d 层。";

	public static final float TIME_TO_USE = 1;

	public static final String AC_SET		= "SET";
	public static final String AC_RETURN	= "RETURN";

	private int returnDepth	= -1;
	private int returnPos;

	{
		name = "劳埃德灯塔";
		image = ItemSpriteSheet.BEACON;

		unique = true;
	}

	private static final String DEPTH	= "depth";
	private static final String POS		= "pos";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( DEPTH, returnDepth );
		if (returnDepth != -1) {
			bundle.put( POS, returnPos );
		}
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		returnDepth	= bundle.getInt( DEPTH );
		returnPos	= bundle.getInt( POS );
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_SET );
		if (returnDepth != -1) {
			actions.add( AC_RETURN );
		}
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		if (action == AC_SET || action == AC_RETURN) {

			if (Dungeon.bossLevel()) {
				hero.spend( LloydsBeacon.TIME_TO_USE );
				GLog.w( TXT_PREVENTING );
				return;
			}

			for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
				if (Actor.findChar( hero.pos + Level.NEIGHBOURS8[i] ) != null) {
					GLog.w( TXT_CREATURES );
					return;
				}
			}
		}

		if (action == AC_SET) {

			returnDepth = Dungeon.depth;
			returnPos = hero.pos;

			hero.spend( LloydsBeacon.TIME_TO_USE );
			hero.busy();

			hero.sprite.operate( hero.pos );
			Sample.INSTANCE.play( Assets.SND_BEACON );

			GLog.i( TXT_RETURN );

		} else if (action == AC_RETURN) {

			if (returnDepth == Dungeon.depth) {
				reset();
				WandOfBlink.appear( hero, returnPos );
				Dungeon.level.press( returnPos, hero );
				Dungeon.observe();
			} else {
				InterlevelScene.mode = InterlevelScene.Mode.RETURN;
				InterlevelScene.returnDepth = returnDepth;
				InterlevelScene.returnPos = returnPos;
				reset();
				Game.switchScene( InterlevelScene.class );
			}


		} else {

			super.execute( hero, action );

		}
	}

	public void reset() {
		returnDepth = -1;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	private static final Glowing WHITE = new Glowing( 0xFFFFFF );

	@Override
	public Glowing glowing() {
		return returnDepth != -1 ? WHITE : null;
	}

	@Override
	public String info() {
		return TXT_INFO + (returnDepth == -1 ? "" : Utils.format( TXT_SET, returnDepth ) );
	}
}

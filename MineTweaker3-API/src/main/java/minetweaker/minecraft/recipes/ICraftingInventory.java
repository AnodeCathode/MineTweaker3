/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package minetweaker.minecraft.recipes;

import minetweaker.minecraft.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 *
 * @author Stan
 */
@ZenClass("minecraft.recipes.ICraftingInventory")
public interface ICraftingInventory {
	@ZenGetter("size")
	public int getSize();
	
	@ZenGetter("width")
	public int getWidth();
	
	@ZenGetter("height")
	public int getHeight();
	
	@ZenMethod
	public IItemStack getStack(int i);
	
	@ZenMethod
	public IItemStack getStack(int x, int y);
	
	@ZenMethod
	public void setStack(int x, int y, IItemStack stack);
	
	@ZenMethod
	public void setStack(int i, IItemStack stack);
}

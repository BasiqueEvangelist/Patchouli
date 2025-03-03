package vazkii.patchouli.mixin.client;

import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Screen.class)
public interface AccessorScreen {

	@Accessor("renderables")
	List<Widget> getRenderables();

	@Accessor("narratables")
	List<NarratableEntry> getNarratables();
}

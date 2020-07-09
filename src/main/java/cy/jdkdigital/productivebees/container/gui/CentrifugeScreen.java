package cy.jdkdigital.productivebees.container.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import cy.jdkdigital.productivebees.ProductiveBees;
import cy.jdkdigital.productivebees.ProductiveBeesConfig;
import cy.jdkdigital.productivebees.container.CentrifugeContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.ArrayList;
import java.util.List;

public class CentrifugeScreen extends ContainerScreen<CentrifugeContainer>
{
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(ProductiveBees.MODID, "textures/gui/container/centrifuge.png");

    public CentrifugeScreen(CentrifugeContainer container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void func_230451_b_(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.font.func_238422_b_(matrixStack, this.title, 8.0F, 6.0F, 4210752);
        this.font.func_238422_b_(matrixStack, this.playerInventory.getDisplayName(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);

        // Draw fluid tank
        this.container.tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(fluidHandler -> {
            int fluidAmount = fluidHandler.getFluidInTank(0).getAmount();

            // Honey fluid level tooltip
            if (isPointInRegion(139, 16, 6, 54, mouseX, mouseY)) {
                List<ITextComponent> tooltipList = new ArrayList<ITextComponent>()
                {{
                    add(new TranslationTextComponent("productivebees.screen.honey_level", fluidAmount));
                }};
                renderTooltip(matrixStack, tooltipList, mouseX - guiLeft, mouseY - guiTop);
            }
        });
    }

    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        assert minecraft != null;
        minecraft.textureManager.bindTexture(GUI_TEXTURE);

        // Draw main screen
        blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        // Draw progress
        int progress = (int) (this.container.tileEntity.recipeProgress * (24 / (float)ProductiveBeesConfig.GENERAL.centrifugeProcessingTime.get()));
        blit(matrixStack, this.guiLeft + 49, this.guiTop + 35, 176, 0, progress + 1, 16);

        // Draw fluid tank
        this.container.tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(fluidHandler -> {
            int fluidAmount = fluidHandler.getFluidInTank(0).getAmount();
            int fluidLevel = (int) (fluidAmount * (52 / 10000F));
            blit(matrixStack, this.guiLeft + 140, this.guiTop + 69, 176, 69, 4, -1 * fluidLevel);
        });
    }
}

package moe.kyuunex.book_dupe;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

public class BookDupe implements ModInitializer {
    private static int dupe(CommandContext<FabricClientCommandSource> context) {
        ItemStack book = new ItemStack(Items.WRITABLE_BOOK, 1);

        book.putSubTag("title", NbtString.of("a"));

        NbtList pages = new NbtList();

        StringBuilder page = new StringBuilder();
        for (int i = 0; i < 21842; i++) {
            page.append((char) 0x0D9E);
        }

        pages.add(0, NbtString.of(page.toString()));
        book.putSubTag("pages", pages);

        ClientPlayNetworkHandler network = context.getSource().getClient().getNetworkHandler();
        assert network != null;
        network.sendPacket(new BookUpdateC2SPacket(book, true, 40));

        return 0;
    }

    @Override
    public void onInitialize() {
        ClientCommandManager.DISPATCHER.register(
            ClientCommandManager.literal("d").executes(BookDupe::dupe)
        );
    }
}
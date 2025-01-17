package braindustry;

import ModVars.Classes.ModAtlas;
import ModVars.Classes.ModEventType;
import ModVars.modVars;
import arc.Core;
import arc.Events;
import arc.func.Boolf;
import arc.func.Func;
import arc.graphics.g2d.TextureRegion;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.CommandHandler;
import braindustry.audio.ModAudio;
import braindustry.core.ModContentLoader;
import braindustry.entities.bullets.ModLightningBulletType;
import braindustry.gen.ModContentRegions;
import braindustry.gen.ModMusics;
import braindustry.gen.ModPlayer;
import braindustry.gen.ModSounds;
import braindustry.graphics.ModShaders;
import braindustry.graphics.g2d.ModBloom;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.ctype.MappableContent;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.bullet.LightningBulletType;
import mindustry.game.EventType;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.game.EventType.DisposeEvent;
import mindustry.gen.EntityMapping;
import mindustry.io.JsonIO;
import mindustry.mod.Mod;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.meta.BuildVisibility;

import static ModVars.modFunc.*;
import static ModVars.modVars.*;
import static mindustry.Vars.*;

public class BraindustryMod extends Mod {


    static Seq<String> names = new Seq<>();

    public BraindustryMod() {
        mods.getScripts().runConsole("importPackage(Packages.braindustry.net.ModNet);");
        EventOn(DisposeEvent.class, (d) -> {
            modUI.dispose();
            Vars.ui.dispose();
        });
        modInfo = Vars.mods.getMod(this.getClass());
        modVars.load();
        Events.on(EventType.Trigger.class, e -> {
            if (e == EventType.Trigger.preDraw) {
                renderUpdate = true;
            }
            if (e == EventType.Trigger.postDraw) {
                renderUpdate = false;
            }
            if (e == EventType.Trigger.draw) {
                if (!headless) {
                    if (!(renderer.bloom instanceof ModBloom)) {
                        renderer.bloom = modBloom.parent(renderer.bloom);
                    }
                }
            }
        });
        EventOn(ClientLoadEvent.class, (e) -> {
            ModAudio.reload();
            constructor();
        });
    }

    public static TextureRegion getIcon() {

//        if (modInfo==null)modInfo = Vars.mods.getMod(mod.getClass());
//        print("modInfo: @",modInfo);
        if (modInfo == null || modInfo.iconTexture == null) return Core.atlas.find("nomap");
        return new TextureRegion(modInfo.iconTexture);
    }

    public static boolean inPackage(String packageName, Object obj) {
        if (packageName == null || obj == null) return false;
        String name;
        try {
            name = obj.getClass().getPackage().getName();
        } catch (Exception e) {
            return false;
        }
        return name.startsWith(packageName + ".");
    }

    @Override
    public void registerServerCommands(CommandHandler handler) {
        modVars.netServer.registerCommands(handler);
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        modVars.netClient.registerCommands(handler);
    }

    void createPlayer() {
        player = ModPlayer.create();
        player.name = Core.settings.getString("name");
        player.color.set(Core.settings.getInt("color-0"));
        if (state.isGame()) {
            player.add();
        }
    }

    public void init() {
        if (!loaded) return;
        getModContent().each(c -> {
            if (c instanceof MappableContent && !headless) ModContentRegions.loadRegions((MappableContent) c);
        });
        createPlayer();
        modVars.init();
        EntityMapping.idMap[12] = ModPlayer::new;
        EntityMapping.nameMap.put("Player", ModPlayer::new);
        EntityMapping.nameMap.put("player", ModPlayer::new);
        Events.fire(new ModEventType.ModInit());
    }

    private void constructor() {
        if (!loaded) return;
        modInfo = Vars.mods.getMod(this.getClass());

        //Seq.with(Blocks.blockForge, Blocks.blockLoader, Blocks.blockUnloader).each(b -> b.buildVisibility = BuildVisibility.shown);
        Blocks.interplanetaryAccelerator.buildVisibility = BuildVisibility.shown;
        Boolf<BulletType> replace = (b) -> (b instanceof LightningBulletType && !(b instanceof ModLightningBulletType));
        Func<BulletType, ModLightningBulletType> newBullet = (old) -> {

            ModLightningBulletType newType = new ModLightningBulletType();
            JsonIO.copy(old, newType);
            return newType;
        };
        Vars.content.each((c) -> {
            if (c instanceof UnlockableContent) {
                UnlockableContent content = (UnlockableContent) c;
                if (content instanceof Block) {
                    if (content instanceof Turret) {
                        if (content instanceof PowerTurret) {
                            PowerTurret powerTurret = (PowerTurret) content;
                            if (replace.get(powerTurret.shootType)) {
                                powerTurret.shootType = newBullet.get(powerTurret.shootType);
                            }
                        } else if (content instanceof ItemTurret) {
                            ItemTurret itemTurret = (ItemTurret) content;
                            ObjectMap<Item, BulletType> toReplace = new ObjectMap<>();
                            for (ObjectMap.Entry<Item, BulletType> entry : itemTurret.ammoTypes.entries()) {
                                if (replace.get(entry.value)) {
                                    toReplace.put(entry.key, entry.value);
                                }
                            }
                            for (ObjectMap.Entry<Item, BulletType> entry : toReplace.entries()) {
                                itemTurret.ammoTypes.put(entry.key, newBullet.get(entry.value));
                            }
                        }
                    }
                }
            }
        });
    }

    public void loadContent() {
        modInfo = Vars.mods.getMod(this.getClass());
        ModAudio.load();
        print("loading mod content...");
        modAtlas = new ModAtlas();
        Events.fire(ModEventType.ModContentLoad.class);
        inTry(() -> {
            if (!headless) ModShaders.init();
        });
        if (!headless) {
            try {
                ModSounds.load();
                ModMusics.load();
            } catch (Exception ignored) {
            }
        }
        new ModContentLoader((load) -> {
            try {
                load.load();
            } catch (NullPointerException e) {
                if (!headless) showException(e);
            }
        });
        Vars.content.each((c) -> {
            if (inPackage("braindustry", c)) {
                modVars.addContent(c);
                if (c instanceof UnlockableContent) checkTranslate((UnlockableContent) c);
            }
        });
        loaded = true;
    }
}

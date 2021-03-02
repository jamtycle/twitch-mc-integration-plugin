package com.TwitchRewards;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Collection;

import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.standard.RequestingUserName;

import com.TwitchIntegration.events.TwitchEvent;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private MongoClient mongo = new MongoClient("<Your MongoDB IP here>", 27017);
    private MongoCollection tevents = mongo.getDatabase("TwitchMCIntegration").getCollection("events");

    @Override
    public void onDisable() {
        mongo.close();
        getLogger().info("Goodbye world!");
    }

    @Override
    public void onEnable() {

        BeginPeekingToDB();
    }

    public void BeginPeekingToDB(){
        Thread t = new Thread(() -> {
            try {
                while(true){
                    Thread.sleep(1000);

                    Player jam = Bukkit.getPlayerExact("Jamtycle");
                    if(jam == null) continue;                    
                    
                    List<Document> info = PeekPeticions();
                    
                    for (Document doc : info) {
                        Document reward = (Document)doc.get("reward");
                        Material mat = RandomizeMaterial(reward.getString("title"));
                        if(mat != null) { jam.getInventory().addItem(new ItemStack(mat, 1)); }
                        else { System.out.println("No se encontró el material :("); }

                        tevents.updateOne(Filters.eq("_id", doc.getObjectId("_id")), new Document("$set", new Document("readed", true)));
                    }                    
                }   
            } catch (Exception e) {
                System.out.println(">> ERROR: " + e.getMessage());
            }
        });
        t.start();
    }

    public Document PeekLastReg(){
        BasicDBObject query = new BasicDBObject();
        query.put("readed", false);
        return (Document)tevents.find(query).limit(1).sort(new Document("_id", -1)).first();
    }

    public List<Document> PeekPeticions(){
        BasicDBObject query = new BasicDBObject();
        query.put("readed", false);
        MongoCursor cursor = tevents.find(query).iterator();
        List<Document> docs = new ArrayList<Document>();
        while (cursor.hasNext()) {
            docs.add((Document)cursor.next());
        }
        return docs;
    }

    public Material RandomizeMaterial(String _typo) {
        try {
            HashMap<Material, Double> info = (HashMap<Material, Double>) this.getClass().getMethod("get" + _typo).invoke(this, null);
            return SelectItem(info);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            return Material.DIRT;
        }
    }

    public Material SelectItem(HashMap<Material, Double> items) {
        Double max = items.entrySet().stream().mapToDouble(x -> x.getValue()).sum();
        Double randomNumber = new Random().nextDouble() * (max - 0d) + 0d;

        Double accumulatedProbability = 0d;
        for (Map.Entry<Material, Double> entry : items.entrySet()) {
            accumulatedProbability += (Double)entry.getValue();
            if (randomNumber <= accumulatedProbability)
                return entry.getKey();
        }
        return null;
    }



    public HashMap<Material, Double> getTronco(){
        HashMap<Material, Double> map = new HashMap<Material, Double>();
        map.put(Material.OAK_LOG, 0.568181818181818d);
        map.put(Material.SPRUCE_LOG, 0.113636363636364d);
        map.put(Material.BIRCH_LOG, 0.102272727272727d);
        map.put(Material.JUNGLE_LOG, 0.0909090909090909d);
        map.put(Material.ACACIA_LOG, 0.0681818181818182d);
        map.put(Material.DARK_OAK_LOG, 0.0568181818181818d);
        return map;
    }

    public HashMap<Material, Double> getPiedra(){
        HashMap<Material, Double> map = new HashMap<Material, Double>();
        map.put(Material.COBBLESTONE, 0.490196078431373d);
        map.put(Material.ANDESITE, 0.117647058823529d);
        map.put(Material.DIORITE, 0.107843137254902d);
        map.put(Material.GRANITE, 0.0980392156862745d);
        map.put(Material.SANDSTONE, 0.0784313725490196d);
        map.put(Material.RED_SANDSTONE, 0.0196078431372549d);
        map.put(Material.INFESTED_COBBLESTONE, 0.0490196078431373d);
        map.put(Material.MOSSY_COBBLESTONE, 0.0392156862745098d);
        return map;
    }

    public HashMap<Material, Double> getTierra(){
        HashMap<Material, Double> map = new HashMap<Material, Double>();
        map.put(Material.DIRT, 0.193548387096774d);
        map.put(Material.COARSE_DIRT, 0.0806451612903226d);
        map.put(Material.GRASS, 0.129032258064516d);
        map.put(Material.SEAGRASS, 0.0483870967741935d);
        map.put(Material.GRASS_PATH, 0.0483870967741935d);
        map.put(Material.TALL_GRASS, 0.0483870967741935d);
        map.put(Material.GRASS_BLOCK, 0.193548387096774d);
        map.put(Material.SUGAR_CANE, 0.0967741935483871d);
        map.put(Material.KELP, 0.0806451612903226d);
        map.put(Material.BAMBOO, 0.0483870967741935d);
        map.put(Material.ORANGE_TULIP, 0.032258064516129d);
        return map;
    }

    public HashMap<Material, Double> getComida() {
        HashMap<Material, Double> map = new HashMap<Material, Double>();
        map.put(Material.SUSPICIOUS_STEW, 0.117647058823529d);
        map.put(Material.BREAD, 0.107843137254902d);
        map.put(Material.CAKE, 0.0490196078431373d);
        map.put(Material.BEEF, 0.0882352941176471d);
        map.put(Material.CHICKEN, 0.0980392156862745d);
        map.put(Material.TROPICAL_FISH, 0.0784313725490196d);
        map.put(Material.COD, 0.0686274509803921d);
        map.put(Material.SALMON, 0.0588235294117647d);
        map.put(Material.POTATO, 0.127450980392157d);
        map.put(Material.POISONOUS_POTATO, 0.0392156862745098d);
        map.put(Material.CARROT, 0.137254901960784d);
        map.put(Material.PUFFERFISH, 0.0294117647058823d);
        return map;
    }

    public HashMap<Material, Double> getCubeta() {
        HashMap<Material, Double> map = new HashMap<Material, Double>();
        map.put(Material.SALMON_BUCKET, 0.142857142857143d);
        map.put(Material.TROPICAL_FISH_BUCKET, 0.157142857142857d);
        map.put(Material.BUCKET, 0.171428571428571d);
        map.put(Material.WATER_BUCKET, 0.128571428571429d);
        map.put(Material.LAVA_BUCKET, 0.114285714285714d);
        map.put(Material.MILK_BUCKET, 0.185714285714286d);
        map.put(Material.PUFFERFISH_BUCKET, 0.1d);
        return map;
    }

    public HashMap<Material, Double> getOre() {
        HashMap<Material, Double> map = new HashMap<Material, Double>();
        map.put(Material.GOLD_ORE, 0.138888888888889d);
        map.put(Material.IRON_ORE, 0.152777777777778d);
        map.put(Material.COAL_ORE, 0.166666666666667d);
        map.put(Material.NETHER_GOLD_ORE, 0.125d);
        map.put(Material.LAPIS_ORE, 0.111111111111111d);
        map.put(Material.DIAMOND_ORE, 0.0972222222222222d);
        map.put(Material.REDSTONE_ORE, 0.0833333333333333d);
        map.put(Material.EMERALD_ORE, 0.0694444444444445d);
        map.put(Material.NETHER_QUARTZ_ORE, 0.0555555555555556d);
        return map;
    }

    public HashMap<Material, Double> getEspecial(){
        HashMap<Material, Double> map = new HashMap<Material, Double>();
        map.put(Material.END_PORTAL_FRAME, 0.0457247370827618d);
        map.put(Material.SOUL_TORCH, 0.00182898948331047d);
        map.put(Material.TORCH, 0.0914494741655236d);
        map.put(Material.TRIPWIRE_HOOK, 0.0823045267489712d);
        map.put(Material.GLASS, 0.0731595793324188d);
        map.put(Material.CHIPPED_ANVIL, 0.100594421582076d);
        map.put(Material.COW_SPAWN_EGG, 0.0640146319158665d);
        map.put(Material.DROWNED_SPAWN_EGG, 0.0548696844993141d);
        map.put(Material.ENDERMAN_SPAWN_EGG, 0.0457247370827618d);
        map.put(Material.PIG_SPAWN_EGG, 0.109739368998628d);
        map.put(Material.VILLAGER_SPAWN_EGG, 0.118884316415181d);
        map.put(Material.BOOK, 0.00137174211248285d);
        map.put(Material.BOOKSHELF, 0.118884316415181d);
        map.put(Material.GOLDEN_APPLE, 0.0182898948331047d);
        map.put(Material.ENCHANTED_GOLDEN_APPLE, 0.00914494741655236d);
        map.put(Material.NETHERITE_SCRAP, 0.0365797896662094d);
        map.put(Material.NETHERITE_INGOT, 0.0274348422496571d);
        return map;
    }
}
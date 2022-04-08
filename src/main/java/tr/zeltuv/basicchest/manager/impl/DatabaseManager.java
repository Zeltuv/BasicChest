package tr.zeltuv.basicchest.manager.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import tr.zeltuv.basicchest.ChestPlugin;
import tr.zeltuv.basicchest.manager.IManager;

import static tr.zeltuv.basicchest.configuration.Configuration.CONFIGURATION;

public class DatabaseManager implements IManager {

    private ChestPlugin plugin;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Override
    public void load(ChestPlugin plugin) {
        this.plugin = plugin;

        initMongoDB();
    }

    @Override
    public void unload() {
        closeConnection();
    }

    public void initMongoDB() {
        mongoClient = MongoClients.create(CONFIGURATION.getString("Mongo-URI"));
        database = mongoClient.getDatabase(CONFIGURATION.getString("Mongo-Database"));
        collection = database.getCollection(CONFIGURATION.getString("Mongo-Collection"));

        try {
            ChestPlugin.log("Connected to the database.");
        } catch (Exception e) {
            ChestPlugin.log("Couldn't connect to the database.");
            mongoClient.close();
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

    }

    public void closeConnection() {
        mongoClient.close();
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection<Document> getCollection() {
        return collection;
    }
}
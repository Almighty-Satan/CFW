# CFW - CuukyFrameWork
### A powerful bukkit framework that runs on 1.7+ and makes coding with bukkit less painful

## Dear developers
In the following I will show you the advantages of using my FrameWork and how much work it can save.
I love feedback and suggestions to improve my framework, but please, leave the coding to me.
I won't accept any pull requests.
This framework has been updated very frequently over the last weeks, it has some new sweet features like a serializer (and more)
but I'm too lazy to log that here.

## Why should I use it for my plugin?
#### **CFW** makes it much easier to make your plugin 1.7+ compatible by having following included:

- A NetworkManager which contains following methods:
```java
  - sendPacket(Packet packet) - Sends a packet
  - getPing() - Gets the ping of a player
  - respawnPlayer() - Respawns a player instantly without waiting for them to press the respawn button
  - getLocale() - Returns locale (language) 
  - sendLinkedMessage(String message, String link) - Sends a clickable chat message
  - sendTitle(String header, String footer) - Sends title header and footer
  - setnTablist(String header, String footer) (1.8+) - Sends tablist header and footer
  - sendActionbar(String message) (1.8+) - Sends a string to the actionbar of the player
  - setAttributeSpeed(double value) (1.9+) - Sets (removes) the hit delay implemented in 1.9
```

- VersionUtils which contain following:
```java
  - getNmsClass() - returns Nms Class of you server version
  - getVersion() - returns your current BukkitVersion (containing .isHigherThan and more)
  - getServerSoftware() - returns Server Software type (containing hasForgeSupport etc.)
```

#### There are more utilities which will help you a lot while working with Bukkit included:
- SuperInventory - Easily create clickable GUI's without much effort
- ItemBuilder - Create ItemStacks with Enchantments, Lore, Displayname, Amount and Material in one line
- Hooks:
  - ItemHooks - Hook items to the inventory of players to any interaction 
  - ChatHooks - Hook to the chat the get the next chat output the player writes
- Configuration:
  - A simple configuration handler (Will be improved)
  - PlaceHolderAPI to easily and efficient replace placeholders in messages
  - Language-System to let the Server-Admins create own language files for every language for your plugin
- Clientadapter
  - Easily handle NameTags, Scoreboards and Tablist -Headers and -Footers
  - Following applies to CustomScoreboards, CustomNametags and CustomTablists:
    - Efficient replaced - if the nametag string didnt change for example, it won't send any new information to the client
    - Can be completely disabled
- Serialization
  - The CFW provides a serialization tool for saving your objects into files
  - It is compatible with every obfuscator I know (Allatori, ProGuard) without changing the config
- MySQL
  - There are mysql classes in the CFW to easily access databases asynchronously while using
    PreparedStatements
  - Also qeueing a MySQL-Qeuery returns an objects which you can synchronize and wait on,
    it will be notified if MySQL has returned the result 

More coming soon!

## How to use it?
### Here's and example of how you intialize the CFW
The CFW just acts as a launcher for all the manager it provides, you can also
just use the manager directly - but using the CFW class is recommendet
```java
this.cuukyFrameWork = new CuukyFrameWork(this);
this.cuukyFrameWork.getClientAdapterManager().setBoardTypeEnabled(CustomBoardType.TABLIST, true);
```

### A BoardUpdateHandler contains following methods
```java
	public ArrayList<String> getTablistHeader();
	
	public ArrayList<String> getTablistFooter();

	public String getScoreboardTitle();
	
	public ArrayList<String> getScoreboardEntries();
	
	public boolean showHeartsBelowName();

	public String getNametagName();
	
	public String getNametagPrefix();
	
	public String getNametagSuffix();
	
	public boolean isNametagVisible(); 
```

### How to hook an item to the hotbar (ItemBuilder example included)
```java
instance.getCuukyFrameWork().getHookManager().registerHook(new ItemHook(player, new ItemBuilder().displayname("§bWähle dein Kit").material(Material.CHEST).build(), 0, new ItemHookHandler() {

	@Override
	public void onInteractEntity(PlayerInteractEntityEvent event) {}

	@Override
	public void onInteract(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR)
			return;

		new KitMenu(player);

		event.setCancelled(true);
	}

	@Override
	public void onEntityHit(EntityDamageByEntityEvent event) {}
		}));
```

### How to create a GUI
```java
public class VoteMainMenu extends SuperInventory {

	public VoteMainMenu(Main instance, Player opener) {
		super("§5Vote", opener, 18, true);

		this.setModifier = false;
		
		instance.getCuukyFrameWork().getInventoryManager().registerInventory(this);
		open();
	}

	@Override
	public boolean onBackClick() {
		return false;
	}

	@Override
	public void onClick(InventoryClickEvent event) {}

	@Override
	public void onClose(InventoryCloseEvent event) {}

	@Override
	public void onInventoryAction(PageAction action) {}

	@Override
	public boolean onOpen() {
		linkItemTo(12, new ItemBuilder().displayname("§eChests").itemstack(new ItemStack(Material.CHEST)).build(), new Runnable() {

			@Override
			public void run() {
				new ChestVoteMenu(opener);
			}
		});

		linkItemTo(14, new ItemBuilder().displayname("§6Loot").itemstack(new ItemStack(Material.GOLDEN_APPLE)).build(), new Runnable() {

			@Override
			public void run() {
				new LootVoteMenu(opener);
			}
		});
		return true;
	}
}
```

### How to check the server version
```java
if(!VersionUtils.getVersion().isHigherThan(BukkitVersion.ONE_7))
	return;
```

### How to check a player's version (requires ProtocolSupport or ViaVersion)
```java
@EventHandler
public void onJoin(PlayerJoinEvent event) {
	System.out.println(event.getPlayer().getName() + " joined using " + MinecraftVersion.getMinecraftVersion(event.getPlayer()).getName());
}
	
```

There is much more to use, but this is the most important stuff

## Have fun using it and thanks for reading

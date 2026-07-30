package br.com.capfood.config;

import br.com.capfood.CapFood;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public final class CapFoodConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("capfood.json");
	private static final Set<String> ALLOWED_FOODS = Set.of(
		"minecraft:cooked_cod",
		"minecraft:cooked_porkchop",
		"minecraft:cooked_mutton",
		"minecraft:cooked_rabbit",
		"minecraft:cooked_chicken",
		"minecraft:cooked_salmon",
		"minecraft:mushroom_stew",
		"minecraft:rabbit_stew",
		"minecraft:beetroot_soup",
		"minecraft:baked_potato",
		"minecraft:cookie",
		"minecraft:cake",
		"minecraft:honey_bottle",
		"minecraft:apple",
		"minecraft:bread",
		"minecraft:pumpkin_pie"
	);

	private static volatile Set<String> selectedFoods = ALLOWED_FOODS;
	private static volatile boolean consumeContainer;
	private static volatile boolean showFoodProperties;
	private static volatile boolean preventRottenFleshWolfFeeding;
	private static volatile boolean preventPrimaryFoodConsumption;
	private static volatile boolean horseIgnoresLeaves;
	private static volatile boolean fasterLeafDecay;

	private CapFoodConfig() {
	}

	public static synchronized void load() {
		if (!Files.exists(CONFIG_PATH)) {
			applyFirstInstallDefaults();
			return;
		}

		try {
			String json = Files.readString(CONFIG_PATH, StandardCharsets.UTF_8);
			ConfigData data = GSON.fromJson(json, ConfigData.class);
			selectedFoods = sanitize(data == null ? List.of() : data.selectedFoods);
			consumeContainer = data != null && data.consumeContainer;
			showFoodProperties = data != null && Boolean.TRUE.equals(data.showFoodProperties);
			preventRottenFleshWolfFeeding = data != null && data.preventRottenFleshWolfFeeding;
			preventPrimaryFoodConsumption = data != null && data.preventPrimaryFoodConsumption;
			horseIgnoresLeaves = data != null && Boolean.TRUE.equals(data.horseIgnoresLeaves);
			fasterLeafDecay = data != null && Boolean.TRUE.equals(data.fasterLeafDecay);
		} catch (IOException | JsonParseException exception) {
			selectedFoods = Set.of();
			consumeContainer = false;
			showFoodProperties = false;
			preventRottenFleshWolfFeeding = false;
			preventPrimaryFoodConsumption = false;
			horseIgnoresLeaves = false;
			fasterLeafDecay = false;
			CapFood.LOGGER.error("Não foi possível carregar {}. Usando valores vanilla.", CONFIG_PATH, exception);
		}
	}

	public static synchronized boolean saveSelection(Collection<Identifier> itemIds) {
		Set<String> sanitized = sanitize(itemIds.stream().map(Identifier::toString).toList());
		if (!save(
			sanitized,
			consumeContainer,
			showFoodProperties,
			preventRottenFleshWolfFeeding,
			preventPrimaryFoodConsumption,
			horseIgnoresLeaves,
			fasterLeafDecay
		)) {
			return false;
		}
		selectedFoods = sanitized;
		return true;
	}

	public static synchronized boolean saveGlobalOptions(
		boolean newConsumeContainer,
		boolean newShowFoodProperties,
		boolean newPreventRottenFleshWolfFeeding,
		boolean newPreventPrimaryFoodConsumption,
		boolean newHorseIgnoresLeaves,
		boolean newFasterLeafDecay
	) {
		if (!save(
			selectedFoods,
			newConsumeContainer,
			newShowFoodProperties,
			newPreventRottenFleshWolfFeeding,
			newPreventPrimaryFoodConsumption,
			newHorseIgnoresLeaves,
			newFasterLeafDecay
		)) {
			return false;
		}
		consumeContainer = newConsumeContainer;
		showFoodProperties = newShowFoodProperties;
		preventRottenFleshWolfFeeding = newPreventRottenFleshWolfFeeding;
		preventPrimaryFoodConsumption = newPreventPrimaryFoodConsumption;
		horseIgnoresLeaves = newHorseIgnoresLeaves;
		fasterLeafDecay = newFasterLeafDecay;
		return true;
	}

	public static boolean consumeContainer() {
		return consumeContainer;
	}

	public static boolean showFoodProperties() {
		return showFoodProperties;
	}

	public static boolean preventRottenFleshWolfFeeding() {
		return preventRottenFleshWolfFeeding;
	}

	public static boolean preventPrimaryFoodConsumption() {
		return preventPrimaryFoodConsumption;
	}

	public static boolean horseIgnoresLeaves() {
		return horseIgnoresLeaves;
	}

	public static boolean fasterLeafDecay() {
		return fasterLeafDecay;
	}

	public static boolean isSelected(Item item) {
		return selectedFoods.contains(BuiltInRegistries.ITEM.getKey(item).toString());
	}

	public static boolean isAllowed(Item item) {
		return ALLOWED_FOODS.contains(BuiltInRegistries.ITEM.getKey(item).toString());
	}

	public static Set<Identifier> selectedIds() {
		LinkedHashSet<Identifier> ids = new LinkedHashSet<>();
		for (String id : selectedFoods) {
			ids.add(Identifier.parse(id));
		}
		return Set.copyOf(ids);
	}

	private static boolean save(
		Set<String> foods,
		boolean shouldConsumeContainer,
		boolean shouldShowFoodProperties,
		boolean shouldPreventRottenFleshWolfFeeding,
		boolean shouldPreventPrimaryFoodConsumption,
		boolean shouldHorseIgnoreLeaves,
		boolean shouldUseFasterLeafDecay
	) {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			Path temporaryPath = CONFIG_PATH.resolveSibling(CONFIG_PATH.getFileName() + ".tmp");
			ConfigData data = new ConfigData(
				foods.stream().sorted().toList(),
				shouldConsumeContainer,
				shouldShowFoodProperties,
				shouldPreventRottenFleshWolfFeeding,
				shouldPreventPrimaryFoodConsumption,
				shouldHorseIgnoreLeaves,
				shouldUseFasterLeafDecay
			);
			Files.writeString(temporaryPath, GSON.toJson(data), StandardCharsets.UTF_8);
			Files.move(temporaryPath, CONFIG_PATH, StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException exception) {
			CapFood.LOGGER.error("Não foi possível salvar {}.", CONFIG_PATH, exception);
			return false;
		}
	}

	private static Set<String> sanitize(Collection<String> ids) {
		LinkedHashSet<String> sanitized = new LinkedHashSet<>();
		if (ids != null) {
			for (String id : ids) {
				if (id != null && ALLOWED_FOODS.contains(id)) {
					sanitized.add(id);
				}
			}
		}
		return Set.copyOf(sanitized);
	}

	private static void applyFirstInstallDefaults() {
		selectedFoods = ALLOWED_FOODS;
		consumeContainer = false;
		showFoodProperties = false;
		preventRottenFleshWolfFeeding = false;
		preventPrimaryFoodConsumption = false;
		horseIgnoresLeaves = false;
		fasterLeafDecay = false;
	}

	private record ConfigData(
		List<String> selectedFoods,
		boolean consumeContainer,
		Boolean showFoodProperties,
		boolean preventRottenFleshWolfFeeding,
		boolean preventPrimaryFoodConsumption,
		Boolean horseIgnoresLeaves,
		Boolean fasterLeafDecay
	) {
	}
}

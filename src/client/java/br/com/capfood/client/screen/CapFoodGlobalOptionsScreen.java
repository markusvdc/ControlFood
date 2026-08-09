package br.com.capfood.client.screen;

import br.com.capfood.client.screen.component.ActionButtons;
import br.com.capfood.client.screen.component.CapBasePanel;
import br.com.capfood.client.screen.component.GlobalOptionEntry;
import br.com.capfood.client.screen.component.LocalizedComponentComparator;
import br.com.capfood.config.CapFoodConfig;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class CapFoodGlobalOptionsScreen extends Screen {
	private static final int MAX_CONTENT_WIDTH = 540;
	private static final int SIDE_MARGIN = 16;
	private static final int OPTIONS_TOP = 137;
	private static final int OPTION_HEIGHT = 30;

	private final Screen parent;
	private final CapBasePanel basePanel = new CapBasePanel();
	private GlobalOptionEntry consumeContainerEntry;
	private GlobalOptionEntry showFoodPropertiesEntry;
	private GlobalOptionEntry markHiddenInformationEntry;
	private GlobalOptionEntry preventRottenFleshWolfFeedingEntry;
	private GlobalOptionEntry preventPrimaryFoodConsumptionEntry;
	private GlobalOptionEntry horseIgnoresLeavesEntry;
	private GlobalOptionEntry fasterLeafDecayEntry;
	private GlobalOptionEntry increasedSaplingBeeNestChanceEntry;
	private GlobalOptionEntry beesSurviveStingingEntry;
	private GlobalOptionEntry showStatusEffectPanelEntry;
	private GlobalOptionEntry showPotionRecipesEntry;
	private boolean consumeContainer;
	private boolean showFoodProperties;
	private boolean markHiddenInformation;
	private boolean preventRottenFleshWolfFeeding;
	private boolean preventPrimaryFoodConsumption;
	private boolean horseIgnoresLeaves;
	private boolean fasterLeafDecay;
	private boolean increasedSaplingBeeNestChance;
	private boolean beesSurviveStinging;
	private boolean showStatusEffectPanel;
	private boolean showPotionRecipes;
	private Component status = Component.empty();
	private int statusColor = 0xFF9CD67A;

	public CapFoodGlobalOptionsScreen(Screen parent) {
		super(Component.translatable("capfood.options.title"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		int contentWidth = Math.min(MAX_CONTENT_WIDTH, this.width - SIDE_MARGIN * 2);
		int left = (this.width - contentWidth) / 2;
		this.consumeContainer = CapFoodConfig.consumeContainer();
		this.showFoodProperties = CapFoodConfig.showFoodProperties();
		this.markHiddenInformation = CapFoodConfig.markHiddenInformation();
		this.preventRottenFleshWolfFeeding = CapFoodConfig.preventRottenFleshWolfFeeding();
		this.preventPrimaryFoodConsumption = CapFoodConfig.preventPrimaryFoodConsumption();
		this.horseIgnoresLeaves = CapFoodConfig.horseIgnoresLeaves();
		this.fasterLeafDecay = CapFoodConfig.fasterLeafDecay();
		this.increasedSaplingBeeNestChance = CapFoodConfig.increasedSaplingBeeNestChance();
		this.beesSurviveStinging = CapFoodConfig.beesSurviveStinging();
		this.showStatusEffectPanel = CapFoodConfig.showStatusEffectPanel();
		this.showPotionRecipes = CapFoodConfig.showPotionRecipes();

		this.consumeContainerEntry = new GlobalOptionEntry(
			left,
			0,
			contentWidth,
			OPTION_HEIGHT,
				Component.translatable("capfood.options.consume_container"),
			"capfood.options.consume_container",
			this.consumeContainer,
			selected -> this.consumeContainer = selected
		);
		this.showFoodPropertiesEntry = new GlobalOptionEntry(
			left,
			0,
			contentWidth,
			OPTION_HEIGHT,
			Component.translatable("capfood.options.show_food_properties"),
			"capfood.options.show_food_properties",
			this.showFoodProperties,
			selected -> this.showFoodProperties = selected
		);
		this.markHiddenInformationEntry = new GlobalOptionEntry(
			left,
			0,
			contentWidth,
			OPTION_HEIGHT,
			Component.translatable("capfood.options.mark_hidden_information"),
			"capfood.options.mark_hidden_information",
			this.markHiddenInformation,
			selected -> this.markHiddenInformation = selected
		);
		this.preventRottenFleshWolfFeedingEntry = new GlobalOptionEntry(
			left,
			0,
			contentWidth,
			OPTION_HEIGHT,
			Component.translatable("capfood.options.prevent_rotten_flesh_wolf_feeding"),
			"capfood.options.prevent_rotten_flesh_wolf_feeding",
			this.preventRottenFleshWolfFeeding,
			selected -> this.preventRottenFleshWolfFeeding = selected
		);
		this.preventPrimaryFoodConsumptionEntry = new GlobalOptionEntry(
			left,
			0,
			contentWidth,
			OPTION_HEIGHT,
			Component.translatable("capfood.options.prevent_primary_food_consumption"),
			"capfood.options.prevent_primary_food_consumption",
			this.preventPrimaryFoodConsumption,
			selected -> this.preventPrimaryFoodConsumption = selected
		);
		this.horseIgnoresLeavesEntry = new GlobalOptionEntry(
			left,
			0,
			contentWidth,
			OPTION_HEIGHT,
			Component.translatable("capfood.options.horse_ignores_leaves"),
			"capfood.options.horse_ignores_leaves",
			this.horseIgnoresLeaves,
			selected -> this.horseIgnoresLeaves = selected
		);
		this.fasterLeafDecayEntry = new GlobalOptionEntry(
			left,
			0,
			contentWidth,
			OPTION_HEIGHT,
			Component.translatable("capfood.options.faster_leaf_decay"),
			"capfood.options.faster_leaf_decay",
			this.fasterLeafDecay,
			selected -> this.fasterLeafDecay = selected
		);
		this.increasedSaplingBeeNestChanceEntry = new GlobalOptionEntry(
			left,
			0,
			contentWidth,
			OPTION_HEIGHT,
			Component.translatable("capfood.options.increased_sapling_bee_nest_chance"),
			"capfood.options.increased_sapling_bee_nest_chance",
			this.increasedSaplingBeeNestChance,
			selected -> this.increasedSaplingBeeNestChance = selected
		);
		this.beesSurviveStingingEntry = new GlobalOptionEntry(
			left,
			0,
			contentWidth,
			OPTION_HEIGHT,
			Component.translatable("capfood.options.bees_survive_stinging"),
			"capfood.options.bees_survive_stinging",
			this.beesSurviveStinging,
			selected -> this.beesSurviveStinging = selected
		);
		this.showStatusEffectPanelEntry = new GlobalOptionEntry(
			left,
			0,
			contentWidth,
			OPTION_HEIGHT,
			Component.translatable("capfood.options.show_status_effect_panel"),
			"capfood.options.show_status_effect_panel",
			this.showStatusEffectPanel,
			selected -> this.showStatusEffectPanel = selected
		);
		this.showPotionRecipesEntry = new GlobalOptionEntry(
			left,
			0,
			contentWidth,
			OPTION_HEIGHT,
			Component.translatable("capfood.options.show_potion_recipes"),
			"capfood.options.show_potion_recipes",
			this.showPotionRecipes,
			selected -> this.showPotionRecipes = selected
		);

		List<GlobalOptionEntry> optionEntries = new ArrayList<>(List.of(
			this.consumeContainerEntry,
			this.showFoodPropertiesEntry,
			this.markHiddenInformationEntry,
			this.preventRottenFleshWolfFeedingEntry,
			this.preventPrimaryFoodConsumptionEntry,
			this.horseIgnoresLeavesEntry,
			this.fasterLeafDecayEntry,
			this.increasedSaplingBeeNestChanceEntry,
			this.beesSurviveStingingEntry,
			this.showStatusEffectPanelEntry,
			this.showPotionRecipesEntry
		));
		Comparator<Component> componentComparator = LocalizedComponentComparator.forCurrentLanguage(this.minecraft);
		optionEntries.sort((first, second) -> componentComparator.compare(first.getMessage(), second.getMessage()));
		for (int index = 0; index < optionEntries.size(); index++) {
			GlobalOptionEntry entry = optionEntries.get(index);
			entry.setY(OPTIONS_TOP + OPTION_HEIGHT * index);
			this.addRenderableWidget(entry);
		}

		int buttonY = this.height - 36;
		ActionButtons actionButtons = new ActionButtons(
			left,
			buttonY,
			contentWidth,
			this::onClose,
			() -> {
			},
			this::toggleAllOptions,
			this::applyOptions,
			true
		);
		actionButtons.addTo(this::addRenderableWidget);
	}

	private void toggleAllOptions() {
		boolean selectAll = !(
			this.consumeContainer
				&& this.showFoodProperties
				&& this.markHiddenInformation
				&& this.preventRottenFleshWolfFeeding
				&& this.preventPrimaryFoodConsumption
				&& this.horseIgnoresLeaves
				&& this.fasterLeafDecay
				&& this.increasedSaplingBeeNestChance
				&& this.beesSurviveStinging
				&& this.showStatusEffectPanel
				&& this.showPotionRecipes
		);
		this.consumeContainerEntry.setSelected(selectAll);
		this.showFoodPropertiesEntry.setSelected(selectAll);
		this.markHiddenInformationEntry.setSelected(selectAll);
		this.preventRottenFleshWolfFeedingEntry.setSelected(selectAll);
		this.preventPrimaryFoodConsumptionEntry.setSelected(selectAll);
		this.horseIgnoresLeavesEntry.setSelected(selectAll);
		this.fasterLeafDecayEntry.setSelected(selectAll);
		this.increasedSaplingBeeNestChanceEntry.setSelected(selectAll);
		this.beesSurviveStingingEntry.setSelected(selectAll);
		this.showStatusEffectPanelEntry.setSelected(selectAll);
		this.showPotionRecipesEntry.setSelected(selectAll);
	}

	private void applyOptions() {
		boolean saved = CapFoodConfig.saveGlobalOptions(
			this.consumeContainer,
			this.showFoodProperties,
			this.markHiddenInformation,
			this.preventRottenFleshWolfFeeding,
			this.preventPrimaryFoodConsumption,
			this.horseIgnoresLeaves,
			this.fasterLeafDecay,
			this.increasedSaplingBeeNestChance,
			this.beesSurviveStinging,
			this.showStatusEffectPanel,
			this.showPotionRecipes
		);
		this.status = Component.translatable(saved ? "capfood.options.status.applied" : "capfood.status.save_failed");
		this.statusColor = saved ? 0xFF9CD67A : 0xFFFF6B6B;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		graphics.fill(0, 0, this.width, this.height, 0xD0101010);

		int contentWidth = Math.min(MAX_CONTENT_WIDTH, this.width - SIDE_MARGIN * 2);
		int left = (this.width - contentWidth) / 2;
		graphics.centeredText(this.font, Component.translatable("capfood.title"), this.width / 2, 14, 0xFFFFFFFF);
		graphics.centeredText(
			this.font,
			Component.translatable("capfood.options.subtitle"),
			this.width / 2,
			29,
			0xFFBDBDBD
		);

		this.basePanel.render(graphics, this.font, left, 47, contentWidth);
		graphics.text(this.font, this.title, left + 4, 123, 0xFFE0E0E0, true);
		super.extractRenderState(graphics, mouseX, mouseY, delta);

		if (!this.status.getString().isEmpty()) {
			graphics.centeredText(this.font, this.status, this.width / 2, this.height - 49, this.statusColor);
		}
	}

	@Override
	public void onClose() {
		this.status = Component.empty();
		this.minecraft.gui.setScreen(this.parent);
	}
}

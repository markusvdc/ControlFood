package br.com.capfood.gameplay;

public interface DawnStockOffer {
	void capfood$setDoubleStock(boolean doubled);
	int capfood$getEffectiveStock();
	void capfood$mergeUsedStock(int additionalUses);
}

## Opções principais

Quando o CAP é aplicado, cada alimento selecionado passa a restaurar exatamente **8 pontos de fome** e **12,8 pontos de saturação**, com tempo de consumo fixo de **1,6 segundo**. O jogador escolhe individualmente quais alimentos recebem esses três valores; não há ajuste separado para fome, saturação ou velocidade.

| Opção | Descrição |
| --- | --- |
| CARNES | Permite aplicar o CAP ao Bacalhau Assado, Costeleta de Porco Assada, Carneiro Assado, Coelho Assado, Frango Assado e Salmão Assado. |
| PRATOS | Permite aplicar o CAP ao Ensopado de Cogumelos, Ensopado de Coelho e Sopa de Beterraba. |
| OUTROS | Permite aplicar o CAP à Batata Assada, Biscoito, Bolo, Frasco de Mel, Maçã, Pão e Torta de Abóbora. |

## Opções globais

| Opção | Descrição |
| --- | --- |
| CONSUMIR RECIPIENTE | Ao consumir um alimento compatível que normalmente devolve um recipiente, o recipiente também é consumido: tigelas dos ensopados e frascos de mel não retornam ao inventário. A regra também se aplica aos frascos de poções bebíveis. Não afeta outros itens com recipientes. |
| MOSTRAR PROPRIEDADES | Enquanto Shift estiver pressionado sobre um alimento compatível, mostra no tooltip seus valores atuais de fome, saturação e tempo de consumo em segundos. Também permite consultar o Bife Assado como referência do CAP. Esta opção apenas exibe valores e não altera os alimentos. |
| PROTEGER LOBOS | Impede exclusivamente o uso de Carne Podre para alimentar lobos. Outros alimentos aceitos pelos lobos continuam funcionando normalmente. |
| DESATIVAR PRIMÁRIOS | Impede o jogador de comer exatamente estes **12 alimentos primários**: Beterraba, Cenoura, Batata, Bife Cru, Frango Cru, Carneiro Cru, Costeleta de Porco Crua, Coelho Cru, Baiacu, Bacalhau Cru, Salmão Cru e Peixe Tropical. Versões cozidas, alimentos preparados e a alimentação de outras entidades não são bloqueados. |
| ATRAVESSAR FOLHAS | Remove a colisão das folhas somente para montarias da família dos cavalos enquanto houver um jogador controlando a montaria. O jogador e as demais entidades continuam colidindo normalmente com as folhas. |
| FOLHAS VELOZES | Depois que o último tronco diretamente conectado de uma árvore natural do Overworld é cortado, faz as folhas atribuídas àquela árvore desaparecerem em velocidade **20× maior que a vanilla**. Quando copas se encostam, estima a origem das folhas pela proximidade entre os troncos cortados e os troncos remanescentes para preservar a árvore vizinha. Se ainda restarem folhas marcadas após **5 segundos**, todas terminam de desaparecer nesse instante. Mantém os drops vanilla, ignora folhas colocadas pelo jogador e não afeta fungos do Nether. |

## Comportamento inicial

Na primeira instalação, todas as opções principais começam ativadas: todos os alimentos compatíveis iniciam selecionados para receber o CAP. Todas as opções globais começam desativadas. Opções globais novas ou ausentes em uma configuração anterior também começam desativadas. Depois que uma escolha é salva explicitamente, ela é preservada nas próximas inicializações.

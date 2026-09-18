# Migração para Minecraft 26.3 — ControlFood

Data: 2026-09-18. Build final concluído com Temurin 25.0.3+9 e Gradle 9.6.0.

Dependências: Loader 0.19.5, Fabric API 0.160.7+26.3 e Mod Menu 21.0.0-beta.1. Loom 1.17.14 e versão 1.0 preservados.

Adaptações: seleção e arraste da lista pela constante oficial do botão esquerdo; assinatura Block.playerDestroy com ServerLevel e ServerPlayer para a queda acelerada das folhas.

Validação: clean build --warning-mode all; revisão estática das assinaturas e chamadas usadas pelos mixins vanilla; formato da receita de defumação comparado com o recurso vanilla 26.3. Não houve inicialização do Minecraft nem validação dos mixins em execução. A API do Modrinth não retornou Inventory Profiles Next Fabric para 26.3 nesta consulta; sua integração opcional continua pendente.

Artefato: build/libs/ControlFood-1.0.jar. Instalação adiada até a atualização da NEBULOSA. Nenhum commit, push ou release. AGENTS.md preservado, ainda com versões compartilhadas da 26.2.

Teste manual pendente: configuração e rolagem; tooltips em GUI Scale 2x; alimentação e recipientes; bolo; fruto do coro no defumador; queda de folhas; gatos, lobos e abelhas; painel de efeitos.

Fontes: https://www.fabricmc.net/2026/09/15/263.html ; https://feedback.minecraft.net/hc/en-us/articles/48913133328013-Minecraft-Java-Edition-26-3 ; APIs Fabric/Modrinth e classes/dados 26.3 resolvidos pelo Loom.

Auditoria estática adicional do JAR: 37 verificações de seletores, parâmetros de callbacks, campos, instruções e constantes; nenhuma divergência nos pontos verificados. Integrações opcionais externas excluídas desta auditoria. Todos os JSONs empacotados passaram na verificação de sintaxe.

Logs desta etapa: build/reports/migration-26.3/build.log e static-audit.txt. JSONs empacotados verificados sintaticamente.

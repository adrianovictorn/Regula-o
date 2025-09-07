# regulacao-marcarcao

$env:SPRING_PROFILES_ACTIVE="conceicao"
./mvnw.cmd spring-boot:run

## Produção (Flyway)
- Perfil de produção (`application-conceicao.properties`) define:
  - `spring.flyway.validate-on-migrate=false` para não falhar por migrations antigas renomeadas (V1–V5).
  - `spring.flyway.clean-disabled=true` para impedir operações destrutivas.
- O script `V15_Adicionando_Proctologista.sql` está propositalmente fora do padrão (nome com um `_`) e será ignorado pelo Flyway.
- Após o deploy, recomenda-se executar `flyway:repair` no banco de produção para alinhar metadados e então reativar validação caso necessário.

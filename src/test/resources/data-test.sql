-- Inserir clientes
INSERT INTO cliente (nome, email, telefone, endereco, data_criacao, ativo) VALUES
('Joao Silva', 'joao@email.com', '(11) 99999-1111', 'Rua A, 123 - Sao Paulo/SP', CURRENT_TIMESTAMP, true),
('Maria Santos', 'maria@email.com', '(11) 99999-2222', 'Rua B, 456 - Sao Paulo/SP', CURRENT_TIMESTAMP,
true),
('Pedro Oliveira', 'pedro@email.com', '(11) 99999-3333', 'Rua C, 789 - Sao Paulo/SP', CURRENT_TIMESTAMP,
true);

-- Inserir restaurantes
INSERT INTO restaurante (nome, categoria, endereco, telefone, taxa_entrega, avaliacao, ativo)
VALUES
('Pizzaria Bella', 'Italiana', 'Av. Paulista, 1000 - Sao Paulo/SP', '(11) 3333-1111', 5.00, 4.5, true),
('Burger House', 'Hamburgueria', 'Rua Augusta, 500 - Sao Paulo/SP', '(11) 3333-2222', 3.50, 4.2,
true),
('Sushi Master', 'Japonesa', 'Rua Liberdade, 200 - Sao Paulo/SP', '(11) 3333-3333', 8.00, 4.8,
true);

-- Inserir produtos
INSERT INTO produto (nome, descricao, preco, categoria, disponivel, restaurante_id) VALUES
-- Pizzaria Bella
('Pizza Margherita', 'Molho de tomate, mussarela e manjericao', 35.90, 'Pizza', true, 1),
('Pizza Calabresa', 'Molho de tomate, mussarela e calabresa', 38.90, 'Pizza', true, 1),
('Lasanha Bolonhesa', 'Lasanha tradicional com molho bolonhesa', 28.90, 'Massa', true, 1),

-- Burger House
('X-Burger', 'Hamburguer, queijo, alface e tomate', 18.90, 'Hamburguer', true, 2),
('X-Bacon', 'Hamburguer, queijo, bacon, alface e tomate', 22.90, 'Hamburguer', true, 2),
('Batata Frita', 'Porcao de batata frita crocante', 12.90, 'Acompanhamento', true, 2),

-- Sushi Master
('Combo Sashimi', '15 pecas de sashimi variado', 45.90, 'Sashimi', true, 3),
('Hot Roll Salmao', '8 pecas de hot roll de salmao', 32.90, 'Hot Roll', true, 3),
('Temaki Atum', 'Temaki de atum com cream cheese', 15.90, 'Temaki', true, 3);

-- Inserir pedidos de exemplo
INSERT INTO pedido (data_pedido, status, valor_total, observacoes,
cliente_id, restaurante_id) VALUES
(NOW(), 'PENDENTE', 54.80, 'Sem cebola na pizza', 1, 1),
(NOW(), 'CONFIRMADO', 41.80, '', 2, 2),
(NOW(), 'ENTREGUE', 78.80, 'Wasabi a parte', 3, 3);

-- Inserir itens dos pedidos
INSERT INTO item_pedido (quantidade, preco_unitario, subtotal, pedido_id, produto_id)
VALUES
-- Pedido 1 (João - Pizzaria Bella)
(1, 35.90, 35.90, 1, 1), -- Pizza Margherita
(1, 28.90, 28.90, 1, 3), -- Lasanha
-- Pedido 2 (Maria - Burger House)
(1, 22.90, 22.90, 2, 5), -- X-Bacon
(1, 18.90, 18.90, 2, 4), -- X-Burger
-- Pedido 3 (Pedro - Sushi Master)
(1, 45.90, 45.90, 3, 7), -- Combo Sashimi
(1, 32.90, 32.90, 3, 8); -- Hot Roll

-- Inserir usuários de teste
INSERT INTO usuario (nome, email, senha, role, ativo, data_criacao, restaurante_id) VALUES
('Admin Sistema', 'admin@delivery.com',
'$2a$10$blZvMgvZ5M7rsAaIiZRS9O4dONth.7ft2rWGvP6WLWuFpP42r1GOS', 'ADMIN', true,
NOW(), null),
('João Cliente', 'joao@email.com',
'$2a$10$blZvMgvZ5M7rsAaIiZRS9O4dONth.7ft2rWGvP6WLWuFpP42r1GOS', 'CLIENTE', true,
NOW(), null),
('Maria Cliente', 'maria@email.com',
'$2a$10$blZvMgvZ5M7rsAaIiZRS9O4dONth.7ft2rWGvP6WLWuFpP42r1GOS', 'CLIENTE', true,
NOW(), null),
('Pizza Palace', 'pizza@palace.com',
'$2a$10$blZvMgvZ5M7rsAaIiZRS9O4dONth.7ft2rWGvP6WLWuFpP42r1GOS', 'RESTAURANTE',
true, NOW(), 1),
('Burger King', 'burger@king.com',
'$2a$10$blZvMgvZ5M7rsAaIiZRS9O4dONth.7ft2rWGvP6WLWuFpP42r1GOS', 'RESTAURANTE',
true, NOW(), 2),
('Carlos Entregador', 'carlos@entrega.com',
'$2a$10$blZvMgvZ5M7rsAaIiZRS9O4dONth.7ft2rWGvP6WLWuFpP42r1GOS', 'ENTREGADOR',
true, NOW(), null);

-- Senha para todos os usuários acima: "senha123"




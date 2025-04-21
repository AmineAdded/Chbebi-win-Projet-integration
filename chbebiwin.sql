-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : lun. 21 avr. 2025 à 21:16
-- Version du serveur : 8.3.0
-- Version de PHP : 8.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `chbebiwin`
--

-- --------------------------------------------------------

--
-- Structure de la table `critere`
--

DROP TABLE IF EXISTS `critere`;
CREATE TABLE IF NOT EXISTS `critere` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `personnalite_id` bigint DEFAULT NULL,
  `contenu` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6eurojjq494ei4dp4j8w2cex1` (`personnalite_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `critere`
--

INSERT INTO `critere` (`id`, `personnalite_id`, `contenu`, `nom`, `icon`) VALUES
(1, 2, 'الاهتمام بالتفاصيل والعمل بطريقة منهجية ومنظمة', 'الدقة والتنظيم', 'mdi-arrange-send-backward'),
(2, 2, 'مهارة عالية في إيجاد حلول منطقية للتحديات المعقدة\r\n', 'حل المشكلات', 'mdi-puzzle'),
(3, 2, 'القدرة على تحليل المواقف بشكل موضوعي وعقلاني', 'التحليل المنطقي', 'mdi-brain');

-- --------------------------------------------------------

--
-- Structure de la table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
CREATE TABLE IF NOT EXISTS `feedback` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `utilisateur_id` bigint DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh93mnw7avcaf82edxmm6ju0n6` (`utilisateur_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `personnalite`
--

DROP TABLE IF EXISTS `personnalite`;
CREATE TABLE IF NOT EXISTS `personnalite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contenu` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `nom_english` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `personnalite`
--

INSERT INTO `personnalite` (`id`, `contenu`, `nom`, `icon`, `image`, `nom_english`) VALUES
(1, 'كيف تستغل شخصيتك الاجتماعية لتكون أكثر تأثيرًا في محيطك.', 'الاجتماعي', NULL, NULL, NULL),
(2, '\nالمفكر هو شخص يعتمد على المنطق والتحليل في اتخاذ القرارات، لا يتأثر بالعواطف. يحب يكون دقيق وحل المشاكل بطريقة منطقية. لكن في المواقف العاطفية أو مع الناس الحساسين، يلقى صعوبة في التفاعل', 'المفكر', 'mdi-brain', '387.jpg', 'Thinker'),
(3, 'كيف تستغل إبداعك لتجد حلولًا مبتكرة لمشاكلك', 'المبدع', NULL, NULL, NULL),
(4, 'كيف تستغل مهاراتك القيادية لتوجيه الفريق نحو النجاح', 'القائد', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `question`
--

DROP TABLE IF EXISTS `question`;
CREATE TABLE IF NOT EXISTS `question` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `test_id` bigint DEFAULT NULL,
  `contenu` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8hejcpbbiq1qje11346akp3uj` (`test_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `question`
--

INSERT INTO `question` (`id`, `test_id`, `contenu`) VALUES
(1, 1, 'كيفاش تتعامل مع المشاكل ؟'),
(2, 1, 'شنوة يحفزك اكثر؟'),
(3, 1, 'كيفاش تتعامل مع الضغط؟'),
(4, 1, '	كيفاش تحب تخدم؟'),
(5, 1, 'شنوة يعني النجاح بالنسبة ليك؟');

-- --------------------------------------------------------

--
-- Structure de la table `quote`
--

DROP TABLE IF EXISTS `quote`;
CREATE TABLE IF NOT EXISTS `quote` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contenu` varchar(255) DEFAULT NULL,
  `nom_auteur` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `reponse`
--

DROP TABLE IF EXISTS `reponse`;
CREATE TABLE IF NOT EXISTS `reponse` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `question_id` bigint DEFAULT NULL,
  `contenu` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp0yq2hox7fyw8rpbhx9t514rt` (`question_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `reponse`
--

INSERT INTO `reponse` (`id`, `question_id`, `contenu`) VALUES
(1, 1, 'نلوج على حلول عملية و سريعة '),
(2, 1, 'نفكر في كل الجوانب قبل ما نقرر'),
(3, 1, 'نبدع حلول جديدة و مبتكرة'),
(4, 1, 'نطلب مساعدة من الناس'),
(5, 2, 'التشجيع من الناس '),
(6, 2, 'تحقيق اهدافي الشخصية'),
(7, 2, 'الابداع و التحديات الجديدة'),
(8, 2, 'تنظيم الأشياء و قيادة الفريق'),
(9, 3, 'نتكلم مع الاصحاب و نفضفض'),
(10, 3, 'ناخذ وقت وحدي و نفكر'),
(11, 3, 'نعمل حاجة جديدة باش ننسى'),
(12, 3, 'نظم وقتي و نحل لمشكلة وحدة وحدة'),
(13, 4, 'مع فريق و نتعاون مع الناس'),
(14, 4, 'وحدي و نركز على التفاصيل'),
(15, 4, 'نجرب أفكار جديدة'),
(16, 4, 'نكون مسؤول على المشروع و نقود الفريق'),
(17, 5, 'العلاقات القوية مع الناس'),
(18, 5, 'تحقيق الأهداف الشخصية'),
(19, 5, 'الابداع و الابتكار'),
(20, 5, 'القيادة و التنظيم');

-- --------------------------------------------------------

--
-- Structure de la table `test`
--

DROP TABLE IF EXISTS `test`;
CREATE TABLE IF NOT EXISTS `test` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nom_test` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `test`
--

INSERT INTO `test` (`id`, `nom_test`) VALUES
(1, 'Test personnalité'),
(2, 'Test personnalité');

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE IF NOT EXISTS `utilisateur` (
  `role` int NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `personnalite_id` bigint DEFAULT NULL,
  `quote_id` bigint DEFAULT NULL,
  `token_expiry` datetime(6) DEFAULT NULL,
  `access_token` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `mdps_compte` varchar(255) NOT NULL,
  `mdps_super_admin` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `reset_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKrma38wvnqfaf66vvmi57c71lo` (`email`),
  KEY `FKqutonim32dys2npikljd07xt8` (`personnalite_id`),
  KEY `FKfq2fphibj01uhe3lftgebpd12` (`quote_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`role`, `id`, `personnalite_id`, `quote_id`, `token_expiry`, `access_token`, `email`, `mdps_compte`, `mdps_super_admin`, `nom`, `reset_token`) VALUES
(0, 3, 2, NULL, NULL, '263471', 'amineadded3@gmail.com', '$2a$10$0SvKPIjgUxU/WMuuG40ryOviyLTSphIUuJEqbt2mgvX28fPcABlym', NULL, 'Amine Added ', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `workshop_events`
--

DROP TABLE IF EXISTS `workshop_events`;
CREATE TABLE IF NOT EXISTS `workshop_events` (
  `date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `lien` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `critere`
--
ALTER TABLE `critere`
  ADD CONSTRAINT `FK6eurojjq494ei4dp4j8w2cex1` FOREIGN KEY (`personnalite_id`) REFERENCES `personnalite` (`id`);

--
-- Contraintes pour la table `feedback`
--
ALTER TABLE `feedback`
  ADD CONSTRAINT `FKh93mnw7avcaf82edxmm6ju0n6` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `question`
--
ALTER TABLE `question`
  ADD CONSTRAINT `FK8hejcpbbiq1qje11346akp3uj` FOREIGN KEY (`test_id`) REFERENCES `test` (`id`);

--
-- Contraintes pour la table `reponse`
--
ALTER TABLE `reponse`
  ADD CONSTRAINT `FKp0yq2hox7fyw8rpbhx9t514rt` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`);

--
-- Contraintes pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD CONSTRAINT `FKfq2fphibj01uhe3lftgebpd12` FOREIGN KEY (`quote_id`) REFERENCES `quote` (`id`),
  ADD CONSTRAINT `FKqutonim32dys2npikljd07xt8` FOREIGN KEY (`personnalite_id`) REFERENCES `personnalite` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

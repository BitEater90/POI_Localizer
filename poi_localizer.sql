-- phpMyAdmin SQL Dump
-- version 4.1.6
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 22 Sty 2016, 15:22
-- Server version: 5.5.36
-- PHP Version: 5.4.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `poi_localizer`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `places`
--

CREATE TABLE IF NOT EXISTS `places` (
  `place_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `vicinity` varchar(400) NOT NULL,
  `formatted_phone_number` varchar(20) DEFAULT '',
  `formatted_address` varchar(100) DEFAULT '',
  `lat` float NOT NULL,
  `lng` float NOT NULL,
  `rating` float NOT NULL DEFAULT '0',
  `international_phone_number` varchar(20) DEFAULT '',
  `website` varchar(100) DEFAULT '',
  `user_id` int(10) unsigned DEFAULT NULL,
  `creation_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `price_level` int(4) DEFAULT NULL,
  `utc_offset` smallint(5) unsigned NOT NULL DEFAULT '0',
  `type_id` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`place_id`),
  UNIQUE KEY `name_latng` (`name`,`lat`,`lng`),
  UNIQUE KEY `name_address` (`name`,`formatted_address`),
  KEY `user_id` (`user_id`),
  KEY `type_id` (`type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=496 ;

--
-- Zrzut danych tabeli `places`
--

INSERT INTO `places` (`place_id`, `name`, `vicinity`, `formatted_phone_number`, `formatted_address`, `lat`, `lng`, `rating`, `international_phone_number`, `website`, `user_id`, `creation_time`, `price_level`, `utc_offset`, `type_id`) VALUES
(224, 'Heaven for Everyone', 'New Heaven', '(02) 9374 4000', 'New Heaven', -29.234, 148.543, 4.7, '+61 2 9374 4000', 'http://www.google.com.au/', 1, '2014-11-18 23:00:00', 1, 0, 34),
(225, 'Czogori', 'Karakorum', '4354354', 'K2, Karakorum, Nepal', 35.8825, 76.5133, 3.8, '(0-34) 454533', 'http://www.czogori.k2.com', 22, '2015-01-14 10:15:16', 2, 0, 34),
(226, 'Broad Peak', 'Karakorum', '4245312', 'K2, Karakorum, Nepal', -14.512, 102.342, 4.2, '(0-34) 454566', 'http://www.broadpeak.k2.com', 22, '2015-01-14 10:17:35', 2, 0, 34),
(227, 'Czomolungma', 'Himalaje', '4245666', 'Mount Everest, Himalaje, Nepal', -14.512, 105.05, 4.2, '(0-34) 4547123', 'http://www.mounteverest.himalaje.com', 22, '2015-01-14 10:21:04', 2, 0, 34),
(453, 'Muzeum Sztygarka', 'Dabrowa Gornicza', '5672365', 'ul. Kosciuszki 25, Dabrowa Gornicza', 12.5, 54.24, 0, '+56 23 5632156', 'http://www.sztygarka.pl', 1, '2016-01-03 23:00:00', 1, 5, 5),
(454, 'Wydzial Aei', 'Gliwice', '2458565', 'ul. Akademicka 16, Gliwice', 50.2867, 18.6765, 7.625, '+48 32 2458565', 'http://www.aei.polsl.pl', 1, '2016-01-04 23:00:00', 1, 11, 82),
(455, 'Domek', 'Dabrowa Gornicza', '2608037', 'ulica Idzikowskiego 147, Dabrowa Gornicza', 50.3891, 19.3115, 0, '+48 32 2608037', 'www.dom.pl', 1, '2016-01-08 23:00:00', 0, 2, 87),
(466, 'Parafia pw Przemienienia Panskiego', 'Dabrowa Gornicza', '2506030', 'ul. Krolewska 20, Dabrowa Gornicza', 50.3876, 19.3135, 5, '+48 32 2506030', 'http://www.parafia.tucznawa.com.pl', 1, '2016-01-10 10:41:31', 1, 1, 23),
(467, 'Szkola Podstawowa nr 23 im Stanislawa Podrazy', 'Dabrowa Gornicza', '2206034', 'ul. Idzikowskiego 139, Dabrowa Gornicza', 50.3893, 19.3136, 0, '+48 32 2206034', 'http://www.szkola23.tucznawa.com.pl', 22, '2016-01-10 10:53:59', 1, 1, 82),
(468, 'Sklep AGD', 'Dabrowa Gornicza', '2608275', 'ul. Idzikowskiego 142, Dabrowa Gornicza', 50.05, 19.12, 0, '+48 32 2608275', 'http://www.agd.sklep.pl', 22, '2016-01-10 11:00:24', 3, 1, 88),
(469, 'Parafia pw Marii Magdaleny', 'Dabrowa Gornicza', '2605273', 'ul. Oswiecenia 34, Dabrowa Gornicza', 50.3796, 19.2615, 0, '+48 32 2206034', 'http://parafiamm.dabrowa.pl', 22, '2016-01-10 11:12:00', 1, 1, 23),
(470, 'Parafia pw Zeslania Ducha Swietego', 'Dabrowa Gornicza', '2640524', 'ul. Wladyslawa Sikorskiego 55, Dabrowa Gornicza', 50.3726, 19.2729, 0, '+48 32 2206034', 'http://parafiamm.dabrowa.pl', 22, '2016-01-10 11:48:46', 1, 1, 23),
(471, 'Parafia pw swietego Antoniego z Padwy', 'Dabrowa Gornicza', '2617670', 'ul. Koscielna 20, Dabrowa Gornicza', 50.336, 19.2272, 0, '+48 32 2617670', 'http://sanktuariumswantoniego.pl', 1, '2016-01-10 11:53:50', 1, 1, 23),
(472, 'Parafia pw Najswietszej Marii Panny Czestochowskiej', 'Dabrowa Gornicza', '2678670', 'ul. Przelotowa 195, Dabrowa Gornicza', 50.3566, 19.3592, 0, '+48 32 2678670', 'http://nmp.leka.pl', 1, '2016-01-10 13:07:11', 1, 1, 23),
(473, 'Kosciol pw Matki Boskiej Anielskiej', 'Dabrowa Gornicza', '2324565', 'ul. Krolowej Jadwigi 30, Dabrowa Gornicza', 50.3233, 19.1928, 0, '+48 32 2324565', 'http://www.mba.dg.pl', 1, '2016-01-10 13:09:50', 1, 1, 23),
(474, 'Radiostacja Gliwicka', 'Gliwice', '3454354', 'ul. Tarnogorska 129, Gliwice', 50.313, 18.688, 10, '+48 32 3454354', 'http://radiostacja.gliwice.pl', 22, '2016-01-13 08:59:22', 1, 1, 66),
(475, 'Zamek Piastowski', 'Gliwice', '2314494', 'ul. Pod Murami 2, Gliwice', 50.2861, 18.6185, 0, '+48 32 3454354', 'http://muzeum.gliwice.pl', 1, '2016-01-13 09:04:36', 1, 1, 66),
(476, 'Muzeum Techniki Sanitarnej', 'Gliwice', '2115643', 'ul. Thomasa Edisona, Gliwice', 50.31, 18.6458, 0, '+48 32 2115643', 'http://muzeum.gliwice.pl', 1, '2016-01-13 09:06:59', 1, 1, 66),
(477, 'Muzeum w Gliwicach - Oddzial Odlewnictwa', 'Gliwice', '3354403', 'ul. Bojkowska 47, Gliwice', 50.2802, 18.6696, 6, '+48 32 3354403', 'http://muzeum.gliwice.pl', 1, '2016-01-13 09:10:35', 1, 1, 66),
(495, 'Nowa Parafia', 'Dabrowa Gornicza', '2586589', 'ulica Idzikowskiego 146, Dabrowa Gornicza', 50.3894, 19.3117, 7.5, '48 32 2586589', 'http://nowa.parafia.pl', 27, '2016-01-20 23:00:00', 0, 1, 23);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `place_events`
--

CREATE TABLE IF NOT EXISTS `place_events` (
  `event_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `summary` varchar(500) NOT NULL DEFAULT '',
  `url` varchar(200) DEFAULT '',
  `place_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`event_id`),
  UNIQUE KEY `name_and_place_id` (`name`,`place_id`),
  KEY `place_id` (`place_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=222 ;

--
-- Zrzut danych tabeli `place_events`
--

INSERT INTO `place_events` (`event_id`, `name`, `start_time`, `summary`, `url`, `place_id`) VALUES
(111, 'Example event 1', '2015-12-02 19:20:13', 'Example Event 1', 'http://example.com/event', 225),
(112, 'Example event 2', '2015-12-02 19:20:26', 'Example Event 2', 'http://example.com/event/2', 225),
(119, 'Example event 3', '2015-12-03 16:12:15', 'Example event 3', 'http://example.com/event/3', 226),
(120, 'Example event 4', '2015-12-03 16:12:49', 'Example event 4', 'http://example.com/event/4', 226),
(121, 'Example event 5', '2015-12-04 16:13:54', 'Example event 5', 'http://example.com/event/5', 226),
(192, 'Nowe zdarzenie', '2016-01-17 19:19:12', 'Takie fajne wydarzenie', 'http://zdarzenie.pl', 225),
(193, 'Kolejne zdarzenie', '2016-01-17 20:36:57', 'Opisalem kolejne zdarzenie', 'http://www.zdarzenie.pl', 225),
(194, 'Egzamin inz', '2016-03-04 09:00:00', 'Trzeci termin egzaminu', 'www.aei.polsl.pl', 454),
(197, 'Wspinaczka', '2016-01-17 20:29:45', 'Wyprawa himalaistow', 'http://himalaje.pl', 225),
(198, 'Suma', '2016-01-17 19:36:28', 'Niedzielna msza poludniowa', 'http://www.mba.dg.pl', 473),
(199, 'Zwiedzanie', '2016-01-17 19:42:14', 'Zwiedzanie muzeow', 'http://muzeum.pl', 477),
(200, 'Nowe_', '2016-01-17 19:32:09', 'Nowe', 'http://nowe.pl', 477),
(201, 'Nowe', '2016-01-17 19:42:14', 'Nowe zdarzenie', 'http://nowe.pl', 477),
(202, 'Nowezd', '2016-01-17 20:18:14', 'Nowezd', 'http://nowezd.pl', 477),
(203, 'Termin_zlozenia_dokumentow', '2016-02-12 12:00:00', 'Termin zlozenia kompletu dokumentow w dziekanacie przed obrona', 'http://www.aei.polsl.pl', 454),
(221, 'Roraty', '2016-01-23 00:00:00', 'Msza poranna', 'http://roraty.nowa.parafia.pl', 495);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `place_reviews`
--

CREATE TABLE IF NOT EXISTS `place_reviews` (
  `review_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `review_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `text` varchar(200) NOT NULL,
  `author_name` varchar(100) NOT NULL,
  `author_url` varchar(200) DEFAULT NULL,
  `rating` float NOT NULL DEFAULT '0',
  `place_id` int(10) unsigned NOT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`review_id`),
  UNIQUE KEY `text` (`text`,`author_name`,`place_id`),
  KEY `place_id` (`place_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=86 ;

--
-- Zrzut danych tabeli `place_reviews`
--

INSERT INTO `place_reviews` (`review_id`, `review_time`, `text`, `author_name`, `author_url`, `rating`, `place_id`, `user_id`) VALUES
(39, '2016-01-04 20:31:47', 'Fajne miejsce', 'Bart Kraw', 'http://kosynier.pl', 2, 225, 1),
(40, '2016-01-10 18:59:09', 'Glowna bazylika w Zaglebiu Dabrowskim. Bardzo okazala, w stylu gotyckim.', 'Bart Kraw', NULL, 8, 473, 1),
(57, '2016-01-10 19:43:38', 'Dobra parafia', 'Bart Kraw', NULL, 4, 466, 1),
(60, '2016-01-10 23:58:15', 'Ladny kosciol', 'Bart Kraw', 'http://www.parafianin.pl', 7, 466, 1),
(62, '2016-01-11 00:15:59', 'Ladny kosciolek', 'Bart Kraw', NULL, 4, 466, 1),
(63, '2016-01-13 10:12:11', 'Recenzja', 'Bart Kraw', 'http://recenzja.pl', 6, 477, 1),
(64, '2016-01-19 23:29:17', 'Doskonale zachowany zabytek. Przypomina o niemieckiej prowokacji z 31 sierpnia 1939, ktora bezposrednio poprzedzila wybuch drugiej wojny swiatowej.', 'Bart Kraw', 'http://radiostacja.gliwice.pl', 10, 474, 1),
(65, '2016-01-19 23:37:01', 'Niemiecka radiostacja sprzed drugiej wojny swiatowej.', 'Bart Kraw', 'http://radiostacja.gliwice.pl', 10, 474, 1),
(66, '2016-01-20 11:09:21', 'Trudne studia ale da sie przezyc.', 'Bart Kraw', 'NULL', 6, 454, 1),
(67, '2016-01-20 12:06:48', 'Jestem pod wrazeniem tego miejsca', 'Krzysiek', NULL, 7, 454, NULL),
(80, '2016-01-20 14:40:21', 'Nowa opinia', 'Arturo', NULL, 8.5, 454, NULL),
(85, '2016-01-21 18:55:21', 'Przepiekny wystroj kosciola.', 'Uzytkownik Testowy', 'http://nowa.parafia.pl', 10, 495, 27);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `place_types`
--

CREATE TABLE IF NOT EXISTS `place_types` (
  `type_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `name_pl` varchar(100) DEFAULT '',
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=97 ;

--
-- Zrzut danych tabeli `place_types`
--

INSERT INTO `place_types` (`type_id`, `name`, `name_pl`) VALUES
(1, 'accounting', 'rachunkowoœæ'),
(2, 'airport', 'lotnisko'),
(3, 'amusement_park', 'park_rozrywki'),
(4, 'aquarium', 'akwarium'),
(5, 'art_gallery', 'galeria_sztuki'),
(6, 'atm', 'bankomat'),
(7, 'bakery', 'piekarnia'),
(8, 'bank', 'bank'),
(9, 'bar', 'bar'),
(10, 'beauty_salon', 'salon_piêknoœci'),
(11, 'bicycle_store', 'sklep_rowerowy'),
(12, 'book_store', 'ksiêgarnia'),
(13, 'bowling_alley', 'krêgielnia'),
(14, 'bus_station', 'przystanek_autobusowy'),
(15, 'cafe', 'kawiarnia'),
(16, 'campground', 'kemping'),
(17, 'car_dealer', 'salon_samochodowy'),
(18, 'car_rental', 'wypo¿yczalnia_samochodów'),
(19, 'car_repair', 'warsztat_samochodowy'),
(20, 'car_wash', 'myjnia_samochodowa'),
(21, 'casino', 'kasyno'),
(22, 'cemetery', 'cmentarz'),
(23, 'church', 'koœció³'),
(24, 'city_hall', 'ratusz_miejski'),
(25, 'clothing_store', 'butik'),
(26, 'convenience_store', 'sklep_ca³odobowy'),
(27, 'courthouse', 's¹d'),
(28, 'dentist', 'dentysta'),
(29, 'department_store', 'dom_handlowy'),
(30, 'doctor', 'lekarz'),
(31, 'electrician', 'elektryk'),
(32, 'electronics_store', 'sklep_elektroniczny'),
(33, 'embassy', 'ambasada'),
(34, 'establishment', 'zak³ad'),
(35, 'finance', 'finanse'),
(36, 'fire_station', 'stra¿_po¿arna'),
(37, 'florist', 'kwiaciarnia'),
(38, 'food', '¿ywnoœæ'),
(39, 'funeral_home', 'dom_pogrzebowy'),
(40, 'furniture_store', 'sklep_meblowy'),
(41, 'gas_station', 'stacja_paliw'),
(42, 'general_contractor', 'g³ówny_wykonawca'),
(43, 'grocery_or_supermarket', 'sklep_spo¿ywczy_lub_supermarket'),
(44, 'gym', 'si³ownia'),
(45, 'hair_care', 'fryzjer'),
(46, 'hardware_store', 'sklep_ze_sprzêtem'),
(47, 'health', 'zdrowie'),
(48, 'hindu_temple', 'œwi¹tynia_hinduistyczna'),
(49, 'home_goods_store', 'sklep_z_wyposa¿eniem_domowym'),
(50, 'hospital', 'szpital'),
(51, 'insurance_agency', 'agencja_ubezpieczeñ'),
(52, 'jewelry_store', 'sklep_jubilerski'),
(53, 'laundry', 'pralnia'),
(54, 'lawyer', 'prawnik'),
(55, 'library', 'biblioteka'),
(56, 'liquor_store', 'sklep_monopolowy'),
(57, 'local_government_office', 'siedziba_samorz¹du'),
(58, 'locksmith', 'œlusarz'),
(59, 'lodging', 'nocleg'),
(60, 'meal_delivery', 'katering'),
(61, 'meal_takeaway', 'posi³ki_na_wynos'),
(62, 'mosque', 'meczet'),
(63, 'movie_rental', 'wypo¿yczalnia_filmów'),
(64, 'movie_theater', 'kino'),
(65, 'moving_company', 'przedsiêbiorstwo'),
(66, 'museum', 'muzeum'),
(67, 'night_club', 'klub nocny'),
(68, 'painter', 'malarz'),
(69, 'park', 'park'),
(70, 'parking', 'parking'),
(71, 'pet_store', 'sklep_zoologiczny'),
(72, 'pharmacy', 'apteka'),
(73, 'physiotherapist', 'fizjoterapeuta'),
(74, 'place_of_worship', 'miejsce_kultu'),
(75, 'plumber', 'hydraulik'),
(76, 'police', 'policja'),
(77, 'post_office', 'poczta'),
(78, 'real_estate_agency', 'agencja_nieruchomoœci'),
(79, 'restaurant', 'restauracja'),
(80, 'roofing_contractor', 'dekarz'),
(81, 'rv_park rv_park\r', ''),
(82, 'school', 'szko³a'),
(83, 'shoe_store', 'sklep_obuwniczy'),
(84, 'shopping_mall', 'galeria_handlowa'),
(85, 'spa', 'spa'),
(86, 'stadium', 'stadion'),
(87, 'storage', 'magazyn'),
(88, 'store', 'sklep'),
(89, 'subway_station', 'stacja_metra'),
(90, 'synagogue', 'synagoga'),
(91, 'taxi_stand', 'postój_taksówek'),
(92, 'train_station', 'stacja_kolejowa'),
(93, 'travel_agency', 'biuro_podró¿y'),
(94, 'university', 'uniwersytet'),
(95, 'veterinary_care', 'weterynarz'),
(96, 'zoo', 'zoo');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `temp_connections`
--

CREATE TABLE IF NOT EXISTS `temp_connections` (
  `connection_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `connection_hash` decimal(21,0) NOT NULL,
  `log_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`connection_id`),
  UNIQUE KEY `connection_hash` (`connection_hash`),
  UNIQUE KEY `user_id_unique` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=605 ;

--
-- Zrzut danych tabeli `temp_connections`
--

INSERT INTO `temp_connections` (`connection_id`, `user_id`, `connection_hash`, `log_time`) VALUES
(499, 25, '948751130372985500000', '2016-01-05 10:36:56'),
(580, 24, '940235120095531600000', '2016-01-13 19:46:49'),
(592, 26, '719635752327400900000', '2016-01-19 13:07:29'),
(603, 27, '802837777978589600000', '2016-01-21 19:00:04'),
(604, 1, '617367673029534200000', '2016-01-22 11:55:21');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `login` varchar(30) NOT NULL,
  `pass` varchar(30) NOT NULL,
  `name` varchar(20) NOT NULL,
  `surname` varchar(30) NOT NULL,
  `mail` varchar(200) NOT NULL,
  `registration_date` date NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `login_id` (`login`),
  UNIQUE KEY `name` (`name`,`surname`),
  UNIQUE KEY `mail` (`mail`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=28 ;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`user_id`, `login`, `pass`, `name`, `surname`, `mail`, `registration_date`, `is_active`) VALUES
(1, 'Kosynier', 'H@s10', 'Bart', 'Kraw', 'biteater2007@gmail.com', '2014-10-20', 1),
(22, 'panda', '@n@k0nd@', 'Mariusz', 'Pandzinski', 'panda61@gmail.com', '2014-12-12', 1),
(24, 'filozof', 'fil123', 'Sokrates', 'Atenski', 'sokrates@atenski.com.pl', '2016-01-03', 1),
(25, 'nowy', 'nowy123', 'Nowy', 'User', 'nowy@user.pl', '2016-01-05', 1),
(26, 'JulCezar', 'Roma12', 'Juliusz', 'Cezar', 'juliusz@cezar.pl', '2016-01-17', 1),
(27, 'Tester', 'Tester123', 'Uzytkownik', 'Testowy', 'uzytkownik@testowy.pl', '2016-01-21', 1);

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `places`
--
ALTER TABLE `places`
  ADD CONSTRAINT `places_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `places_ibfk_2` FOREIGN KEY (`type_id`) REFERENCES `place_types` (`type_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `places_ibfk_3` FOREIGN KEY (`type_id`) REFERENCES `place_types` (`type_id`) ON UPDATE CASCADE;

--
-- Ograniczenia dla tabeli `place_events`
--
ALTER TABLE `place_events`
  ADD CONSTRAINT `place_events_ibfk_1` FOREIGN KEY (`place_id`) REFERENCES `places` (`place_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ograniczenia dla tabeli `place_reviews`
--
ALTER TABLE `place_reviews`
  ADD CONSTRAINT `place_reviews_ibfk_1` FOREIGN KEY (`place_id`) REFERENCES `places` (`place_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `place_reviews_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL;

--
-- Ograniczenia dla tabeli `temp_connections`
--
ALTER TABLE `temp_connections`
  ADD CONSTRAINT `temp_connections_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

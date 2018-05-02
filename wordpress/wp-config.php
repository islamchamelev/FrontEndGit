<?php
/**
 * The base configuration for WordPress
 *
 * The wp-config.php creation script uses this file during the
 * installation. You don't have to use the web site, you can
 * copy this file to "wp-config.php" and fill in the values.
 *
 * This file contains the following configurations:
 *
 * * MySQL settings
 * * Secret keys
 * * Database table prefix
 * * ABSPATH
 *
 * @link https://codex.wordpress.org/Editing_wp-config.php
 *
 * @package WordPress
 */

// ** MySQL settings - You can get this info from your web host ** //
/** The name of the database for WordPress */
define('DB_NAME', 'wordpress2');

/** MySQL database username */
define('DB_USER', 'shakeel');

/** MySQL database password */
define('DB_PASSWORD', 'umis566');

/** MySQL hostname */
define('DB_HOST', 'localhost');

/** Database Charset to use in creating database tables. */
define('DB_CHARSET', 'utf8');

/** The Database Collate type. Don't change this if in doubt. */
define('DB_COLLATE', '');

/**#@+
 * Authentication Unique Keys and Salts.
 *
 * Change these to different unique phrases!
 * You can generate these using the {@link https://api.wordpress.org/secret-key/1.1/salt/ WordPress.org secret-key service}
 * You can change these at any point in time to invalidate all existing cookies. This will force all users to have to log in again.
 *
 * @since 2.6.0
 */
define('AUTH_KEY',         'T8<B?uA=+|ONIge04hO8Nti5|]O`5giQsI$tXhqaJ9mGo7gZ>rh7%|:CJ9{Q=US]');
define('SECURE_AUTH_KEY',  '^|O?AgU<d.gQvjoEOpCjb6n+`j~V!!<`6t+H }y_:DZAv|x }=nz+Ki(huO~H4|:');
define('LOGGED_IN_KEY',    'Z~p-4?N{/&_+8>P$m[8%x-AcmT}T024)EPa%G3gMSC0phnbPX#M8f;jI>~zHcM]@');
define('NONCE_KEY',        '>B^xj|d$}Sq~h8_% 8QOf%/~Y[bQ}zk@N(cA*(}A]+{_~YfD=++zvWd3Q+m4<Lvq');
define('AUTH_SALT',        '5-MgSQ98_8  1n*fkhI][.WT70)7xgkvL<;Ea]+v>z;/{glO}3BF1-<-Gr8c}YPH');
define('SECURE_AUTH_SALT', 'U?!eQ<xO1h:yMKgav|8{PMh}=o>Y/r>L6+x7Fek<5~^6-!o;a5a)]phiO>si%1)b');
define('LOGGED_IN_SALT',   '+)e&.2HO>GYkOu<aMD=(f+$G;+3V+s.u-;-G@J=&i5:z*<X7uyVsAU!Y_!ys$^9!');
define('NONCE_SALT',       '5LGM5cgj5,XJlPtiV4-?.[x]x|f[ck$` E:TE>m60x_Q^l-]aL8%0bQL0?y~[AMO');

/**#@-*/

/**
 * WordPress Database Table prefix.
 *
 * You can have multiple installations in one database if you give each
 * a unique prefix. Only numbers, letters, and underscores please!
 */
$table_prefix  = 'wp_';

/**
 * For developers: WordPress debugging mode.
 *
 * Change this to true to enable the display of notices during development.
 * It is strongly recommended that plugin and theme developers use WP_DEBUG
 * in their development environments.
 *
 * For information on other constants that can be used for debugging,
 * visit the Codex.
 *
 * @link https://codex.wordpress.org/Debugging_in_WordPress
 */
define('WP_DEBUG', false);

/* That's all, stop editing! Happy blogging. */

/** Absolute path to the WordPress directory. */
if ( !defined('ABSPATH') )
	define('ABSPATH', dirname(__FILE__) . '/');

/** Sets up WordPress vars and included files. */
require_once(ABSPATH . 'wp-settings.php');

define('WP_RABBITMQ_PASSWORD', 'admin');



<a href="https://www.spigotmc.org/resources/loginreloaded.81861/"><img src="https://raw.githubusercontent.com/8bitFra/LoginReloaded/master/icon%20-%20byChuckchee.png" title="SpigotPage" alt="SpigotPage" width="200" height="200"></a>



# LoginReloaded

> Simple plugin for Minecraft Bukkit/Spigot that allows you to protect your world from undesiderated users with a login prompt. Versions: [1.13-1.17]


## Update Ideas

- MySql Support

---
## Login Features
- Highly configurable
- Automatic login with configurable KeepAlive
- Console log disabled for the /login - /register - /changepw - /logout commands
-  Password saved using a message-digest algorithm (MD5) in order to improve security
- Sqlite db save
- Ability to choose how many accounts a user can register


## Commands

- /register
  - Register your account.
- /login
  - Login your account.
- /logout
  - Logout your account.
- /changepw
  - Change your login password.
- /unregister
  - Delete a specific user's password.
- /check
  - Allows to get some useful informations of a specific user.

## Permissions

- loginreloaded.registration
  - Allows you to use the /register - /login - /logout commands. (Default: true)
- loginreloaded.changepw
  - Allows you to use the /changepw command. (Default: true)
- loginreloaded.unregister
  - Allows you to use the /unregister command. (Default: op)
- loginreloaded.check
  - Allows you to use the /check command. (Default: op)

## Contributors
- CoredTV's SuperLogin plugin as a starting point.
  - <a href="https://www.spigotmc.org/resources/superlogin-a-login-system.20324/">His Plugin</a>
- Icon provided by Chuckchee on this <a href="https://icons-for-free.com/lock-131982518830500474/">Website</a>.
---


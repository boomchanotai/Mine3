# Mine3Permission

Mine3Permission is a permission manager plugin for Mine3, driven by the address concept, designed to manage player permissions effectively.

## Prerequisite

- Mine3Lib

## Compatibility

- Java 21
- Minecraft 1.21

## Config

1. You have to set postgres `host`, `username`, `password`
2. Config your `groups` each groups consist of
   - `default` (boolean) - set default group for first join user (can be only one group)
   - `permissions` (list) - list of permissions
   - `metadata`
     - `prefix` (string)
     - `suffix` (string)

## Commands & Permissions

| Commands                                  | Permission                   | Description                    |
| ----------------------------------------- | ---------------------------- | ------------------------------ |
| `/permission has <permission>`            | `mine3.permission.has`       | check yourself has permission. |
| `/permission has <permission> <address>`  | `mine3.permission.has`       | check address has permission.  |
| `/permission set-group <group> <address>` | `mine3.permission.set-group` | set player group.              |
| `/permission reload`                      | `mine3.permission.reload`    | reload plugin config.          |

## Technical Information & For Development

### Integration Note:

- **Group Assignment**: Groups will be added to a player after the `PlayerAuthEvent` has been triggered.
- **Custom Authentication Integration**: If you're developing your own authentication plugin, ensure that you trigger the `PlayerAuthEvent` by calling the `void setPlayer(Address address, Player player)` method from **Mine3Lib**.
- For more detailed information, please refer to the [Mine3Lib documentation](/Mine3Lib/README.md).

This approach ensures that your custom plugin is compatible with Mine3Permission and that permissions are correctly assigned to players.

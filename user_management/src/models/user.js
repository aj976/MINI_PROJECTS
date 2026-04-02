import { DataTypes } from "sequelize";
import sequelize from "../config/connection.js";

const User = sequelize.define(
  "User",
  {
    id: {
      type: DataTypes.INTEGER,
      primaryKey: true,
      autoIncrement: true
    },
    name: {
      type: DataTypes.STRING,
      allowNull: false
    },
    email: {
      type: DataTypes.STRING,
      allowNull: false,
      unique: true,
      validate: {
        isEmail: true
      }
    },
    age: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    city: {
      type: DataTypes.STRING,
      allowNull: true
    }
  },
  {
    tableName: "users",
    timestamps: false
  }
);

export default User;
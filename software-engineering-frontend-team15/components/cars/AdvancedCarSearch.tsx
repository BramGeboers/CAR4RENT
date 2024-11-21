import CarService from '@/services/CarService';
import { Car } from '@/types';
import { useTranslation } from 'next-i18next';
import React, { useRef, useState,forwardRef, ForwardedRef, useImperativeHandle, MutableRefObject } from 'react';

const AdvancedCarSearch = forwardRef(({ setCars, page, setTotalPages, handleOpen }: { setCars: any, page: number, setTotalPages: React.Dispatch<React.SetStateAction<number>>, handleOpen: (isOpen: boolean) => void }, ref) => {
    const { t } = useTranslation();
    
    const [brand, setBrand] = useState<string |null>(null);
    const [model, setModel] = useState<string|null>(null);
    const [types, setTypes] = useState<string|null>(null);
    const [licensePlate, setLicensePlate] = useState<string|null>(null);
    const [numberOfSeats, setNumberOfSeats] = useState<number|null>(null);
    const [numberOfChildSeats, setNumberOfChildSeats] = useState<number|null>(null);
    const [foldingRearSeat, setFoldingRearSeat] = useState<boolean|null>(null);
    const [towBar, setTowBar] = useState<boolean|null>(null);
    const [mileage, setMileage] = useState<number|null>(null);
    const [fuel, setFuel] = useState<number|null>(null);
    const [fuelCapacity, setFuelCapacity] = useState<number|null>(null);
    const [fuelEstimatedConsumption, setFuelEstimatedConsumption] = useState<number|null>(null);
    const [pricePerKm, setPricePerKm] = useState<number|null>(null);
    const [pricePerLiterFuel, setPricePerLiterFuel] = useState<number|null>(null);


    useImperativeHandle(ref, () => ({
        handleSubmit,
        handleReset,
        
    }));

    const handleReset = () => {
        setBrand(null);
        setModel(null);
        setTypes(null);
        setLicensePlate(null);
        setNumberOfSeats(null);
        setNumberOfChildSeats(null);
        setFoldingRearSeat(null);
        setTowBar(null);
        setMileage(null);
        setFuel(null);
        setFuelCapacity(null);
        setFuelEstimatedConsumption(null);
        setPricePerKm(null);
        setPricePerLiterFuel(null);
    };

    

  const handleSubmit = async () => {
    console.log("submitting");
    // console.log({ brand, model, types, licensePlate, numberOfSeats, numberOfChildSeats, foldingRearSeat, towBar, mileage, fuel, fuelCapacity, fuelEstimatedConsumption, pricePerKm, pricePerLiterFuel });
    // CarService.searchCars({ brand, model, types, licensePlate, numberOfSeats, numberOfChildSeats, foldingRearSeat, towBar, mileage, fuel, fuelCapacity, fuelEstimatedConsumption, pricePerKm, pricePerLiterFuel });
    //if all null, fetch all cars
    if (!brand && !model && !types && !licensePlate && !numberOfSeats && !numberOfChildSeats && !foldingRearSeat && !towBar && !mileage && !fuel && !fuelCapacity && !fuelEstimatedConsumption && !pricePerKm && !pricePerLiterFuel){
        const data = await CarService.getAllCars(page);
        if (data.status === 200){
            const json = await data.json();
            setTotalPages(json.totalPages);
            setCars(json.content);
        }
        return;
    }
    const data = await CarService.searchCars({ brand, model, types, licensePlate, numberOfSeats, numberOfChildSeats, foldingRearSeat, towBar, mileage, fuel, fuelCapacity, fuelEstimatedConsumption, pricePerKm, pricePerLiterFuel },page);
    if (data.status === 200){
        const json = await data.json();
        console.log(json);
        setTotalPages(json.totalPages);
        setCars(json.content);
    }
};
// For booleans
const setFoldingRearSeatcheck = (value: boolean) => {
    if (value === false) {
      setFoldingRearSeat(null);
    } else {
      setFoldingRearSeat(value);
    }
  };
  
  const setTowBarcheck = (value: boolean) => {
    if (value === false) {
      setTowBar(null);
    } else {
      setTowBar(value);
    }
  };
  
  // For strings
  const setBrandcheck = (value: string) => {
    if (value === '') {
      setBrand(null);
    } else {
      setBrand(value);
    }
  };
  
    const setModelcheck = (value: string) => {
        if (value === '') {
        setModel(null);
        } else {
        setModel(value);
        }
    };

    const setTypescheck = (value: string) => {
        if (value === '') {
        setTypes(null);
        } else {
        setTypes(value);
        }
    };

    const setLicensePlatecheck = (value: string) => {
        if (value === '') {
        setLicensePlate(null);
        } else {
        setLicensePlate(value);
        }
    };

    // For numbers

    const setNumberOfSeatscheck = (value: number) => {
        if (value === 0) {
        setNumberOfSeats(null);
        } else {
        setNumberOfSeats(value);
        }
    };

    const setNumberOfChildSeatscheck = (value: number) => {
        if (value === 0) {
        setNumberOfChildSeats(null);
        } else {
        setNumberOfChildSeats(value);
        }
    };

    const setMileagecheck = (value: number) => {
        if (value === 0) {
        setMileage(null);
        } else {
        setMileage(value);
        }
    };

    const setFuelcheck = (value: number) => {
        if (value === 0) {
        setFuel(null);
        } else {
        setFuel(value);
        }
    };

    const setFuelCapacitycheck = (value: number) => {
        if (value === 0) {
        setFuelCapacity(null);
        } else {
        setFuelCapacity(value);
        }
    };

    const setFuelEstimatedConsumptioncheck = (value: number) => {
        if (value === 0) {
        setFuelEstimatedConsumption(null);
        } else {
        setFuelEstimatedConsumption(value);
        }
    };

    const setPricePerKmcheck = (value: number) => {
        if (value === 0) {
        setPricePerKm(null);
        } else {
        setPricePerKm(value);
        }
    };

    const setPricePerLiterFuelcheck = (value: number) => {
        if (value === 0) {
        setPricePerLiterFuel(null);
        } else {
        setPricePerLiterFuel(value);
        }
    };



  return (
<div className="bg-gray-200">
        <div className="flex flex-row justify-center items-center pt-10 pb-5 px-8 mx-auto">
          <form
            onSubmit={(e)=>{
                e.preventDefault();
                handleSubmit();}}
            onReset={handleReset}
            className="grid grid-cols-4 gap-4 mx-auto min-w-[1276px] justify-between bg-white p-5 rounded-xl"
          >
            <div className="flex flex-col ">
        <label className='mb-1'>
            {t("cars.brand")}
        </label>
        <input className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md" type="text" onChange={(e) => {if(e.target.value===''){
                setBrand(null)
            }else{setBrand(e.target.value)}}} />
        </div>
        <div className="flex flex-col ">
        <label className='mb-1'>
            {t("cars.model")}

        </label>
            <input className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md" type="text" onChange={(e) => setModelcheck(e.target.value)} />
</div>
<div className="flex flex-col ">
        <label className='mb-1'>
            {t("cars.types")}
            </label>
            <input className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md" type="text" onChange={(e) => setTypescheck(e.target.value)} />
            </div>
            <div className="flex flex-col ">
        <label className='mb-1'>

            {t("cars.licensePlate")}
        </label>
            <input className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md" type="text" onChange={(e) => setLicensePlatecheck(e.target.value)} />
        </div>
        <div className="flex flex-col ">
        <label className='mb-1'>
            {t("cars.numberOfSeats")}
        </label>
            <input className="w-[100%] bg-gray-200 p-1 mb-2 focus:outline-none rounded-md" type="number" onChange={(e) => setNumberOfSeatscheck(Number(e.target.value))} />
        </div>
        <div className="flex flex-col ">
        <label className='mb-1'>
            {t("cars.numberOfChildSeats")}
        </label>
            <input className="w-[100%] bg-gray-200 p-1 mb-2 focus:outline-none rounded-md" type="number" onChange={(e) => setNumberOfChildSeatscheck(Number(e.target.value))} />
        </div>
        <div className="flex flex-col  items-start">
        <label className='mb-1'>
            {t("cars.foldingRearSeat")}
        </label>
            <input className='w-[30px] h-[30px] flex align-text-top' type="checkbox" onChange={(e) => setFoldingRearSeatcheck(e.target.checked)} />
        </div>
        <div className="flex flex-col  items-start">
        <label className='mb-1'>
            {t("cars.towBar")}
        </label>
        <input className='w-[30px] h-[30px] flex align-text-top' type="checkbox" onChange={(e) => setTowBarcheck(e.target.checked)} />

        </div>
        {/* <div className="flex flex-col ">
        <label className='mb-1'>
            {t("cars.mileage-placeholder")}
        </label>
            <input className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md" type="number" onChange={(e) => setMileagecheck(Number(e.target.value))} />
        </div>
        <div className="flex flex-col ">
        <label className='mb-1'>
            {t("cars.fuel")}
            <input className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md" type="number" onChange={(e) => setFuelcheck(Number(e.target.value))} />

        </label></div>
        <div className="flex flex-col ">
        <label className='mb-1'>
            {t("cars.fuelCapacity")}
            <input className="w-[100%] bg-gray-200 p-1 mb-1 focus:outline-none rounded-md" type="number" onChange={(e) => setFuelCapacitycheck(Number(e.target.value))} />
        </label></div>
        <div className="flex flex-col ">
        <label className='mb-1'>
            {t("cars.fuelEstimatedConsumption")}
            <input className="w-[100%] bg-gray-200 p-1 mb-2 focus:outline-none rounded-md" type="number" onChange={(e) => setFuelEstimatedConsumptioncheck(Number(e.target.value))} />

        </label></div>
        <div className="flex flex-col ">
        <label className='mb-1'>
            {t("cars.pricePerKm")}
            <input className="w-[100%] bg-gray-200 p-1 mb-2 focus:outline-none rounded-md" type="number" onChange={(e) => setPricePerKmcheck(Number(e.target.value))} />
        </label></div>
        <div className="flex flex-col ">
        <label className='mb-1'>
            {t("cars.pricePerLiterFuel")}
            <input className="w-[100%] bg-gray-200 p-1 mb-2 focus:outline-none rounded-md" type="number" onChange={(e) => setPricePerLiterFuelcheck(Number(e.target.value))} />
        </label></div> */}
        <div className='flex flex-row gap-4'>
            <input className='rounded-xl font-semibold border-red-500 border-2 bg-red-500 w-[150px] h-[55px] flex items-center justify-center text-lg hover:bg-white  ease-in-out duration-200 cursor-pointer' type="reset" value="Cancel" onClick={() => handleOpen(false)} 
        />
            <input className='rounded-xl font-semibold border-blue-400 border-2 bg-blue-400 w-[150px] h-[55px] flex items-center justify-center text-lg hover:bg-white  ease-in-out duration-200 cursor-pointer' type="submit" value="Search" />
        </div>
    </form>
    </div>
    </div>
  );
});

export default AdvancedCarSearch;
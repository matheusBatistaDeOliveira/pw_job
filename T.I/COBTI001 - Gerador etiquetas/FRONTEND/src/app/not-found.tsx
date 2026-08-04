import Link from "next/link"
import localFont from 'next/font/local'

const fontRegular = localFont({ src: '../../assets/fonts/Poppins-Regular.ttf' });
const fontBold = localFont({ src: '../../assets/fonts/Poppins-Bold.ttf' });

export default function notfound() {
    return (
        <div className="flex flex-row items-center justify-center" style={{ width: '100%', height: '100vh', backgroundColor:"#FBFBF8" }}>
            <div className="flex flex-col items-center justify-center gap-3" style={{ width: "50%" }}>
                <h1 className={`${fontBold.className} text-6xl`} style={{ color: '#087B84', }}>Oops!</h1>
                <h1 className={`${fontBold.className} text-2xl`} style={{ color: '#009CA6', }}>Parece que você está longe.</h1>
                <h1 className={`${fontBold.className} text-2xl`} style={{ color: '#009CA6', }}>A página que você acessou não existe.</h1>
                <Link href={"/"}>
                    <div className="" >
                        <button
                            className={`${fontRegular.className} text-white p-3 rounded-2xl cursor-pointer`}
                            style={{ backgroundColor: "#7EB339" }}>
                            Go Home
                        </button>
                    </div>
                </Link>
            </div>

            <div className="flex flex-col items-center justify-center" style={{ width: '50%' }}>
                <h1 className={`${fontBold.className} text-8xl`}
                style={{color:'#087B84'}}>
                    404
                    </h1>
                <img src="/imgNotFound.svg" alt="" width={"60%"} />
            </div>
        </div>
    )
}